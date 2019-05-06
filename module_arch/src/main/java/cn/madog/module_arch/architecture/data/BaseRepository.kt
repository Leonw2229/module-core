package cn.madog.module_arch.architecture.data

/**
 * 基础的Repository抽象，内部做了View层创建和销毁的事件传递
 * @author leon.w
 */
open class BaseRepository : BaseDataSource {

    private var repositories: MutableList<BaseDataSource>? = mutableListOf()

    protected fun <RS : BaseDataSource> setRepository(repository: RS): RS {
        repositories?.add(repository)
        return repository
    }

    override fun onCreate() {
    }

    override fun onDestroy() {

        repositories?.forEach {
            it.onDestroy()
        }
        repositories?.clear()
        repositories = null
    }
}