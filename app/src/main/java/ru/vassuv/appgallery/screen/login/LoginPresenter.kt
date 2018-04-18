package ru.vassuv.appgallery.screen.login

import android.content.Intent
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yandex.authsdk.YandexAuthException
import ru.vassuv.appgallery.App
import ru.vassuv.appgallery.fabric.FrmFabric
import ru.vassuv.appgallery.utils.SharedData
import ru.vassuv.appgallery.utils.atlibrary.saveString
import ru.vassuv.appgallery.utils.routing.Router

@InjectViewState
class LoginPresenter : MvpPresenter<LoginView>() {
    val REQUEST_LOGIN_SDK = 100

    override fun onFirstViewAttach() {
        viewState.startActivityForResult(App.yandex.createLoginIntent(App.context, null), REQUEST_LOGIN_SDK)
    }

    fun authResult(requestCode: Int, resultCode: Int, data: Intent?) = when (requestCode) {
        REQUEST_LOGIN_SDK -> try {
            SharedData.TOKEN.saveString(App.yandex.extractToken(resultCode, data)?.value ?: "")
            Router.navigateTo(FrmFabric.START.name)
        } catch (e: YandexAuthException) {
            e.printStackTrace()
        }
        else -> Unit
    }
}
