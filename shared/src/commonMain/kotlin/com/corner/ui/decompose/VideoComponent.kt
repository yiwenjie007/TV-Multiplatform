package com.corner.ui.decompose

import com.arkivanov.decompose.value.MutableValue
import com.corner.catvod.enum.bean.Vod
import com.corner.catvodcore.bean.Type
import org.slf4j.Logger

interface VideoComponent {
    var log:Logger

    var model:MutableValue<Model>

    fun homeLoad()

    fun loadMore()

    data class Model(
        var homeVodResult: MutableSet<Vod>? = null,
        var homeLoaded: Boolean = false,
        var classList: MutableSet<Type> = mutableSetOf(),
        var currentClass: Type? = null,
        var page: Int = 1,
        var isRunning: Boolean = false,
        val prompt:String = ""
    ){

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Model

            if (homeVodResult != other.homeVodResult) return false
            if (homeLoaded != other.homeLoaded) return false
            if (classList != other.classList) return false
            if (currentClass != other.currentClass) return false
            if (page != other.page) return false
            if (isRunning != other.isRunning) return false
            if (prompt != other.prompt) return false

            return true
        }

        override fun hashCode(): Int {
            var result = 0
            homeVodResult?.forEach{result = 31 * result + it.hashCode()}
            result = 31 * result + homeLoaded.hashCode()
            result = 31 * result + classList.hashCode()
            classList.forEach{ result = 31 * result + it.hashCode()}
            result = 31 * result + (currentClass?.hashCode() ?: 0)
            result = 31 * result + page
            result = 31 * result + isRunning.hashCode()
            result = 31 * result + prompt.hashCode()
            return result
        }


    }
}