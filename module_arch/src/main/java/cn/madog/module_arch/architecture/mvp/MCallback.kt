package cn.madog.module_arch.architecture.mvp

interface MCallback<D> {
    fun onSuccess(data: D?)
    /**
     * @param code Type = String , if value index 0 = '-' as System error else Server error code
     * @param message Type = String , System error info = SimpleChinese
     */
    fun onFailure(code: String, message: String)
}