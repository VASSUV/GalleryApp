package ru.vassuv.appgallery.screen.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import ru.vassuv.appgallery.App
import ru.vassuv.appgallery.R
import ru.vassuv.appgallery.utils.atlibrary.BaseFragment


class LoginFragment : BaseFragment(), LoginView {
    @InjectPresenter
    lateinit var presenter: LoginPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.authResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
