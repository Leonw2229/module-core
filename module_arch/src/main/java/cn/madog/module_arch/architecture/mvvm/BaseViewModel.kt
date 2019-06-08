package cn.madog.module_arch.architecture.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.madog.module_arch.architecture.data.BaseDataSource
import cn.madog.module_arch.entity.ModelActionEvent

/**
 * @description
 *
 * @author leon.w
 * @create 2019/4/3 10:29 AM
 * @update 2019/4/3 10:29 AM
 * @version 1
 */
abstract class BaseViewModel : ViewModel() {
    protected val uiState by lazy { UiStateResource() }

    /**
     * 页面弹框的显示隐藏状态
     */
    fun getUiState(): MutableLiveData<ModelActionEvent> = uiState.getUIState()

    private var repositories: MutableList<BaseDataSource>? = mutableListOf()

    /**
     * 设置资源仓库方式，使用callback返回，可改为使用LiveData做返回数据
     */
    fun <RS : BaseDataSource> setRepository(repository: RS): RS {
        repositories?.add(repository)
        return repository
    }

    override fun onCleared() {
        repositories?.forEach {
            it.onDestroy()
        }
        repositories?.clear()
        repositories = null
        super.onCleared()
    }
}