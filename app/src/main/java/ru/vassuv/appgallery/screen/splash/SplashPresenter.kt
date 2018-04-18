package ru.vassuv.appgallery.screen.splash

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import ru.vassuv.appgallery.fabric.FrmFabric
import ru.vassuv.appgallery.utils.SharedData
import ru.vassuv.appgallery.utils.atlibrary.getString
import ru.vassuv.appgallery.utils.routing.Router

@InjectViewState
class SplashPresenter : MvpPresenter<SplashView>() {
    private lateinit var job: Job

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        job = launch(UI) {
            (1 downTo 1).forEach {
                viewState.setText(it.toString())
                delay(500)
            }
            if(SharedData.TOKEN.getString() == null)
                Router.newRootScreen(FrmFabric.LOGIN.name)
            else
                Router.newRootScreen(FrmFabric.START.name)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
