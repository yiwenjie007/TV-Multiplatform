package com.corner.ui.decompose.component

import SiteViewModel
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.corner.catvodcore.bean.Type
import com.corner.catvodcore.config.api
import com.corner.ui.decompose.VideoComponent
import com.corner.ui.scene.hideProgress
import com.corner.ui.scene.showProgress
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DefaultVideoComponent(componentContext: ComponentContext):VideoComponent, ComponentContext by componentContext, BackHandlerOwner {

    private val _log = LoggerFactory.getLogger("Video")

    override var log: Logger = _log

    private val _model = MutableValue(VideoComponent.Model())

    override var model: MutableValue<VideoComponent.Model> = _model

    override fun homeLoad() {
        SiteViewModel.viewModelScope.launch {
            showProgress()
            try {
                if (!_model.value.homeLoaded) {
                    if (api?.home == null) return@launch
                    var list = SiteViewModel.homeContent().list.toMutableSet()
                    var classList = SiteViewModel.result.value.types.toMutableSet()
                    val currentClass = classList.first()

                    // 有首页内容
                    if (list.isNotEmpty()) {
                        classList = (mutableSetOf(Type.home()) + classList).toMutableSet()
                    } else {
                        val types = SiteViewModel.result.value.types
                        if (types.isEmpty()) return@launch
                        SiteViewModel.categoryContent(
                            api?.home?.value?.key ?: "",
                            types.get(0).typeId,
                            _model.value.page.toString(),
                            true,
                            HashMap()
                        )
                        list = SiteViewModel.result.value.list.toMutableSet()
                    }
//                    if (_model.value.classList.size > 0) {
//                        _model.update { it.copy(currentClass = _model.value.classList.first()) }
//                    }
                    _model.value.homeLoaded = true
                    _model.update { it.copy(homeVodResult = list, currentClass = currentClass, classList = classList) }
                }
            } catch (e: Exception) {
                log.error("homeLoad", e)
            } finally {
                hideProgress()
            }
        }
        _model.value.page += 1
    }

}