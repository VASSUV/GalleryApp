package ru.vassuv.appgallery.screen.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vassuv.appgallery.R
import ru.vassuv.appgallery.utils.atlibrary.BaseFragment

import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : BaseFragment(), SplashView {
    @InjectPresenter
    lateinit var presenter: SplashPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun setText(text: String) {
        textView.text = text
    }
}
