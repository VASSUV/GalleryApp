package ru.vassuv.appgallery.fabric

import android.os.Bundle
import ru.vassuv.appgallery.screen.login.LoginFragment
import ru.vassuv.appgallery.screen.splash.SplashFragment
import ru.vassuv.appgallery.screen.start.StartFragment
import ru.vassuv.appgallery.utils.atlibrary.BaseFragment
import kotlin.reflect.KClass

enum class FrmFabric(private val kClass: KClass<out BaseFragment>) {
    SPLASH(SplashFragment::class),
    LOGIN(LoginFragment::class),
    START(StartFragment::class),

    EMPTY(SplashFragment::class);

    fun create(data: Bundle): BaseFragment {
        val fragment = kClass.java.constructors[0].newInstance() as BaseFragment
        fragment.arguments = data
        return fragment
    }

    companion object {
        @JvmStatic
        fun valueOf(kClass: KClass<out BaseFragment>) = values().find {it.kClass == kClass}
    }
}