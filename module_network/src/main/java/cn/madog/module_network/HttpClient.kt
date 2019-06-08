package cn.madog.module_network

import com.hdfjy.module_network.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 * HttpClient.get(moduleName) 获取到当前module的网络请求实例，如果moduleName不传则默认获取的是default请求实例，可用作各模块之间通用的网络请求
 * 1.default实例下 debug开发下默认开启 OkHttp Logging拦截器
 * 2.default时以第一次调用时传的baseUrl为准，后续所有的调用全都以第一次调用时传的baseUrl为准
 *   如果想要使用其他baseUrl请传入moduleName
 *   如果当前moduleName已经创建并且有更改baseUrl的需求，可在@POST或@GET()注解里的地址填写完整地址进行更改
 *   如有大批量接口都需要更改baseUrl，则请传入一个新的moduleName，可定义为当前模块名加其他字段
 *   如需要重置当前Retrofit请调用 HttpClient.Builder() 进行构造，默认只要调用Builder()就会替换原有的对象，如果没有则添加上
 *
 * @sample 使用示例 object LoginService{
 *                       private val client by lazy { HttpClient.get(LoginConstants.BASE_URL) }
 *
 *                       fun client(): LoginApiService{
 *                           return client.create(LoginApiService::class.java)
 *                       }
 *                       interface LoginApiService{
 *
 *                       }
 *                   }
 *
 * @author leon.w
 * @create 2019/4/4 1:59 PM
 * @update 2019/4/4 1:59 PM
 * @version 1
 */
object HttpClient {

    private val moduleList: MutableMap<String, Retrofit> = mutableMapOf()

    /**
     * 获取当前module的网络请求实例，如果没有或者不传则返回的是默认的实例，默认的实例只存在一个，所有module可共用
     * @param baseUrl 备用，如果没有当前Module的Retrofit实例就使用baseUrl进行初始化，
     * @param moduleName 模块的名称，不传默认为 default
     */
    fun get(baseUrl: String, debugModel: Boolean = BuildConfig.DEBUG, moduleName: String = "default"): Retrofit {
        return if (moduleList.containsKey(moduleName) && moduleList[moduleName]?.baseUrl()?.toString() == baseUrl) {
            moduleList[moduleName]!!
        } else {
            initRetrofit(baseUrl, debugModel, moduleName)
        }
    }

    fun Builder(): HttpBuilder {
        return HttpBuilder()
    }

    private fun initRetrofit(baseUrl: String, debugModel: Boolean, moduleName: String): Retrofit {
        return HttpBuilder()
            .baseUrl(baseUrl)
            .setDebugModel(debugModel)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build(moduleName)
    }

    fun build(builder: HttpBuilder, debugModel: Boolean, moduleName: String = "default"): Retrofit {

        val retrofitBuilder = Retrofit.Builder()

        var client = builder.client
        if (client == null) {
            client = OkHttpClient.Builder()
        }

        if (builder.httpLoggingInterceptor == null) {
            if (debugModel) {
                // Log信息拦截器
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                //设置 Debug Log 模式
                client.addInterceptor(loggingInterceptor)
            }
        }

        builder.httpLoggingInterceptor?.apply {
            if (debugModel) {
                client.addInterceptor(this)
            }
        }

        builder.authenticator?.apply {
            client.authenticator(this)
        }

        client.also {
            if (builder.httpLoggingInterceptor != null) {
                it.addInterceptor(builder.httpLoggingInterceptor)
            }

            if (builder.authenticator != null) {
                it.authenticator(builder.authenticator)
            }

            if (builder.cache != null) {
                it.cache(builder.cache)
            }

            if (builder.readTimeout > 0) {
                it.readTimeout(builder.readTimeout, builder.readTimeUnit)
            }

            if (builder.writeTimeout > 0) {
                it.writeTimeout(builder.writeTimeout, builder.writeTimeUnit)
            }

            if (builder.connectionPool != null) {
                it.connectionPool(builder.connectionPool)
            }

            if (builder.cookieJar != null) {
                it.cookieJar(builder.cookieJar)
            }

            if (builder.dispatcher != null) {
                it.dispatcher(builder.dispatcher)
            }

            if (builder.dns != null) {
                it.dns(builder.dns)
            }

            if (builder.hostnameVerifier != null) {
                it.hostnameVerifier(builder.hostnameVerifier)
            }

            if (!builder.interceptors.isNullOrEmpty()) {
                builder.interceptors.forEach { interceptor ->
                    it.addInterceptor(interceptor)
                }
            }

            if (builder.networkInterceptor != null) {
                it.addNetworkInterceptor(builder.networkInterceptor)
            }

            if (builder.proxyAuthenticator != null) {
                it.proxyAuthenticator(builder.proxyAuthenticator)
            }

            if (builder.sslSocketFactory != null && builder.trustManager != null) {
                it.sslSocketFactory(builder.sslSocketFactory, builder.trustManager)
            }

        }

        retrofitBuilder.baseUrl(builder.baseUrl)

        builder.callAdapterFactoryList.forEach {
            retrofitBuilder.addCallAdapterFactory(it)
        }

        builder.converterFactoryList.forEach {
            retrofitBuilder.addConverterFactory(it)
        }

        retrofitBuilder.client(client.build())

        val retrofit = retrofitBuilder.build()

        moduleList[moduleName] = retrofit

        return retrofit
    }

    class HttpBuilder {
        internal var authenticator: Authenticator? = null
        internal var connectionPool: ConnectionPool? = null
        internal var cookieJar: CookieJar? = null
        internal var dispatcher: Dispatcher? = null
        internal var dns: Dns? = null
        internal var hostnameVerifier: HostnameVerifier? = null
        internal var sslSocketFactory: SSLSocketFactory? = null
        internal var trustManager: X509TrustManager? = null
        internal var proxyAuthenticator: Authenticator? = null
        internal var cache: Cache? = null
        internal var baseUrl: HttpUrl? = null
        internal var callAdapterFactoryList: MutableList<CallAdapter.Factory> = mutableListOf()
        internal var client: OkHttpClient.Builder? = null
        internal var converterFactoryList: MutableList<Converter.Factory> = mutableListOf()

        internal var readTimeout: Long = 120L
        internal var readTimeUnit: TimeUnit = TimeUnit.SECONDS

        internal var writeTimeout: Long = 120L
        internal var writeTimeUnit: TimeUnit = TimeUnit.SECONDS

        internal var httpLoggingInterceptor: HttpLoggingInterceptor? = null
        internal var networkInterceptor: Interceptor? = null

        internal var interceptors: MutableList<Interceptor> = mutableListOf()

        internal var moduleName: String? = null

        internal var debugModel: Boolean = BuildConfig.DEBUG

        fun setDebugModel(debugModel: Boolean): HttpBuilder {
            this.debugModel = debugModel
            return this
        }

        fun authenticator(authenticator: Authenticator): HttpBuilder {
            this.authenticator = authenticator
            return this
        }

        fun connectionPool(connectionPool: ConnectionPool): HttpBuilder {
            this.connectionPool = connectionPool
            return this
        }

        fun cookieJar(cookieJar: CookieJar): HttpBuilder {
            this.cookieJar = cookieJar
            return this
        }

        fun dispatcher(dispatcher: Dispatcher): HttpBuilder {
            this.dispatcher = dispatcher
            return this
        }

        fun dns(dns: Dns): HttpBuilder {
            this.dns = dns
            return this
        }

        fun hostnameVerifier(hostnameVerifier: HostnameVerifier): HttpBuilder {
            this.hostnameVerifier = hostnameVerifier
            return this
        }

        fun sslSocketFactory(sslSocketFactory: SSLSocketFactory, trustManager: X509TrustManager? = null): HttpBuilder {
            this.sslSocketFactory = sslSocketFactory
            this.trustManager = trustManager
            return this
        }

        fun proxyAuthenticator(proxyAuthenticator: Authenticator): HttpBuilder {
            this.proxyAuthenticator = proxyAuthenticator
            return this
        }


        fun cache(cache: Cache): HttpBuilder {
            this.cache = cache
            return this
        }

        fun baseUrl(baseUrl: HttpUrl): HttpBuilder {
            this.baseUrl = baseUrl
            return this
        }

        /**
         * 设置的String值得BaseUrl最终会转换成HttpUrl，方法内调用setBaseUrl(baseUrl: HttpUrl)进行设置url
         */
        fun baseUrl(baseUrl: String): HttpBuilder {

            val httpUrl = HttpUrl.parse(baseUrl)
                ?: throw IllegalArgumentException("Illegal URL: $baseUrl")
            baseUrl(httpUrl)
            return this
        }

        fun addCallAdapterFactory(factory: CallAdapter.Factory): HttpBuilder {
            if (!callAdapterFactoryList.contains(factory)) {
                callAdapterFactoryList.add(factory)
            }
            return this
        }

        fun setClient(client: OkHttpClient.Builder): HttpBuilder {
            this.client = client
            return this
        }

        fun addConverterFactory(factory: Converter.Factory): HttpBuilder {
            if (!converterFactoryList.contains(factory)) {
                converterFactoryList.add(factory)
            }
            return this
        }

        fun setReadTimeout(timeout: Long, unit: TimeUnit): HttpBuilder {
            readTimeout = timeout
            readTimeUnit = unit
            return this
        }

        fun setWriteTimeout(timeout: Long, unit: TimeUnit): HttpBuilder {
            writeTimeout = timeout
            writeTimeUnit = unit
            return this
        }

        fun setLoggingInterceptor(interceptor: HttpLoggingInterceptor): HttpBuilder {
            httpLoggingInterceptor = interceptor
            return this
        }

        fun addNetworkInterceptor(interceptor: Interceptor): HttpBuilder {
            this.networkInterceptor = interceptor
            return this
        }

        fun addInterceptor(interceptor: Interceptor): HttpBuilder {
            val contains = interceptors.contains(interceptor)
            if (!contains) {
                interceptors.add(interceptor)
            }
            return this
        }

        fun build(moduleName: String = "default"): Retrofit {
            return build(this, debugModel, moduleName)
        }
    }


}