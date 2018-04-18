package ru.vassuv.appgallery.screen.login

import android.content.Intent
import com.arellomobile.mvp.MvpView

interface LoginView : MvpView {
    fun startActivityForResult(intent: Intent, requestCode: Int)
}
