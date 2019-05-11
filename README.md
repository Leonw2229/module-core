

# ModuleCore

[![](https://jitpack.io/v/Leonw2229/module-core.svg)](https://jitpack.io/#Leonw2229/module-core)



一个项目基础架构的简单抽象，各模块间没有耦合关系，目前包含如下模块

- ModuleManager: 简单的模块间解耦操作，用于组件化实施，仅仅是简单的解耦
- ModuleArch: 项目常用架构，MVP和MVVM
- ModuleNetwork: 多模块统一的网络库创建，根据ModuleName和BaseUrl区分各模块的客户端



## 使用方式

**先设置jitpack仓库**

```gradle
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```



### ModuleManager模块

简单的模块间解耦操作，用于组件化实施，仅仅是简单的解耦

```groovy
implementation 'com.github.Leonw2229.module-core:module_manager:v+'
```

**使用方式**：

1、在当前Module创建一个XXXApplication类然后实现 **ModuleMApplication** 接口

2、在当前module里面的assets目录下创建一个module_m_xxx.json开头的json文件，请保证xxx部分在所有模块中是唯一的，然后再此json文件中填写如下内容

```json
{
  "moduleName": "module_xxx",
  "moduleClass": "cn.madog.module_xxx.XXXApplication",
  "moduleHandleRequestClass": "",
  "moduleWeight": 1
}
```
- moduleName 模块名称
- moduleClass 当前Module需要代理创建的Application
- moduleHandleRequestClass 模块对外请求的处理类，暂时无用，并未实现
- moduleWeight 未实现


3、在主Module中也可以说壳Module中的Application中ModuleM初始化，别忘了初始化MainApplication到Manifest中

```kotlin
class MApplication : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(baseContext)

        ModuleM.init(this)
        ModuleM.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        ModuleM.onCreate()
    }
  
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        ModuleM.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        ModuleM.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        ModuleM.onTrimMemory(level)
    }
```



### ModuleNetwork模块

统一管理各Module中的网络请求客户端，不同的模块可单独创建自己的客户端，也可使用统一的客户端

```groovy
implementation 'com.github.Leonw2229.module-core:module_network:v+'
```

**使用方式:**

```kotlin
private val client by lazy { HttpClient.get(AdminConstants.BASE_URL, BuildConfig.DEBUG,"admin") } // 有默认参数，最后个参数是当前Module的名称，根据当前Module的名称会创建出一个新的客户端，如果已经有当前ModuleName的客户端了则共用此实例
```

另有Builder方式创建，Builder方式创建一次就会产生一个新的实例



### ModuleArch模块

基础架构模块，内有MVP和MVVM的基础架构，MVVM不包含DataBinding，仅仅使用LiveData和ViewModel框架

```groovy
implementation 'com.github.Leonw2229.module-core:module_arch:v+'
```

**MVP使用方式:**

```kotlin
// 推荐创建一个契约类来管理

interface LiveDetailContract {
    interface View : IMvpContract.IView {
        fun resultCourseDetail(courseLiveDetail: CourseLiveDetail)
    }

    interface Presenter : IMvpContract.IPresenter<View> {
        fun getCourseDetail(userId: Int, courseId: Int)
    }
}

class LiveDetailActivity : BaseMvpActivity<LiveDetailContract.View, LiveDetailContract.Presenter>(), LiveDetailContract.View{}

class LiveDetailPresenter: BaseMvpPresenter<LiveDetailContract.View>(),LiveDetailContract.Presenter{
  private val repository by lazy { setRepository(VideoListRepository()) }
}

class VideoListRepository: BaseRepository(),VideoListDataSource.RemoteVideoListDataSource{
    private val remoteModel by lazy { setRepository(VideoRemoteModel()) }
}

class VideoRemoteModel: BaseModel(),VideoListDataSource.RemoteVideoListDataSource{
  
    override fun getLiveList(status: Int, currentPage: Int, callback: MCallback<LiveListResult>) {
        HttpClient.instance.getLiveList(status, currentPage)
                .schedulers()
                .subscribe(VideoObserver(callback,getCompositeDisposable()))
    }
}
```



**MVVM使用方式:**

```kotlin
// activity同样方式
class StudentTaskFragment : BaseFragmentMVVM(){
  private val viewModel by lazy { setViewModel(StudentTaskViewModel::class.java) }
  
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      
        viewModel.taskHomeList.observe(this, Observer {
            viewSwipeLayout.isRefreshing = false
            adapter.setNewData(it)
        })
    }
} 

class StudentTaskViewModel : BaseViewModel() {
  private val repository by lazy { setRepository(StudentTaskRepository()) }

    val taskHomeList: MutableLiveData<List<TaskHomeEntity>> = MutableLiveData()
  
    fun getTasks() {
        repository.getTasks(userInfo.studentStudentDto!!.code.orEmpty(),
                userInfo.studentStudentDto!!.hospitalCode.orEmpty(),
                userInfo.studentStudentDto!!.hospitalDepartmentCode.orEmpty(),
                {
                    taskHomeList.value = it.returnData
                }, { code, message ->
            				uiState.showError(code,message)
        				}
        )
    }
}
```

Module里面的Repository跟MVP方式是相同的



## 已包含的依赖列表

一般情况下是不会有任何影响的，implementation 方式，外部Module是访问不到的，需要使用请自行添加

```gradle
##  module_arch
implementation 'androidx.appcompat:appcompat:1.1.0-alpha05'
implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
implementation 'io.reactivex.rxjava2:rxjava:2.2.8'

implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
implementation 'org.jetbrains.anko:anko-commons:0.10.8'
implementation 'androidx.core:core-ktx:1.2.0-alpha01'
implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0-alpha01'
implementation 'com.github.lmyDevs:sweet-alert-dialog:v1.4.4'
implementation 'com.gyf.immersionbar:immersionbar:2.3.3'
implementation 'com.alibaba:arouter-api:1.4.1'
implementation 'com.blankj:utilcodex:1.23.7'
    
##  module_network
implementation 'com.google.code.gson:gson:2.8.5'
implementation 'com.squareup.okhttp3:okhttp:3.14.1'
implementation 'com.squareup.okhttp3:logging-interceptor:3.14.1'
implementation 'com.squareup.retrofit2:retrofit:2.5.0'
implementation 'com.squareup.retrofit2:converter-gson:2.5.0'
implementation 'com.squareup.retrofit2:adapter-rxjava2:2.5.0'
implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
implementation 'io.reactivex.rxjava2:rxjava:2.2.8'
implementation"org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
implementation 'org.jetbrains.anko:anko-commons:0.10.8'

implementation 'androidx.appcompat:appcompat:1.1.0-alpha05'
```



