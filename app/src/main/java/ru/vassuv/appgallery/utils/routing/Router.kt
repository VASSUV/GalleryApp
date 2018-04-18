package ru.vassuv.appgallery.utils.routing

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.FragmentTransaction
import ru.terrakok.cicerone.BaseRouter
import ru.terrakok.cicerone.commands.*
import ru.vassuv.appgallery.utils.UiListener
import ru.vassuv.appgallery.utils.routing.animate.AnimateForward

object Router: BaseRouter() {
    var onNewRootScreenListener: ((screenKey: String) -> Unit)? = null
    var onBackScreenListener: (() -> Unit)? = null
    var uiListener: UiListener? = null

    fun backTo(screenKey: String) = executeCommands(BackTo(screenKey))

    fun navigateTo(screenKey: String, data: Any? = Bundle()) = executeCommands(Forward(screenKey, data))

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun navigateToWithAnimate(screenKey: String,  animate: FragmentTransaction.() -> Unit) {
        executeCommands(AnimateForward(screenKey, Bundle(), animate))
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun navigateToWithAnimate(screenKey: String, data: Any?, animate: FragmentTransaction.() -> Unit) {
        executeCommands(AnimateForward(screenKey, data, animate))
    }

    fun replaceScreen(screenKey: String, data: Any? = Bundle()) = executeCommands(Replace(screenKey, data))

    fun newScreenChain(screenKey: String, data: Any? = Bundle()) {
        executeCommands(BackTo(null), Forward(screenKey, data))
    }

    fun newRootScreen(screenKey: String, data: Any? = Bundle()) {
        executeCommands(BackTo(null), Replace(screenKey, data))
        onNewRootScreenListener?.invoke(screenKey)
    }

    fun exit() {
        onBackScreenListener?.invoke()
        executeCommands(Back())
    }

    fun exitWithMessage(message: String) {
        executeCommands(Back(), SystemMessage(message))
    }
}

