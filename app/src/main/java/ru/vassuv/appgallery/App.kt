package ru.vassuv.appgallery

import android.app.Application
import android.content.Context
import ru.terrakok.cicerone.Cicerone
import ru.vassuv.appgallery.utils.atlibrary.Logger
import ru.vassuv.appgallery.utils.routing.Navigator
import ru.vassuv.appgallery.utils.routing.Router
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk



class App(val cicerone: Cicerone<Router> = Cicerone.create(Router)) : Application() {

    companion object {
        private lateinit var app: App

        val context: Context
            get() = app.applicationContext

        val yandex: YandexAuthSdk
            get() = YandexAuthSdk(YandexAuthOptions(app.applicationContext, true))

        fun log(vararg args: Any?) = Logger.trace(args)
        fun logExc(text: String, exception: Throwable) = Logger.traceException(text, exception)
        fun setNavigationHolder(navigator: Navigator?) = app.cicerone.navigatorHolder.setNavigator(navigator)
        fun resetNavigator() = app.cicerone.navigatorHolder.removeNavigator()
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}