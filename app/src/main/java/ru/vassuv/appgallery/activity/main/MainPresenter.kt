package ru.vassuv.appgallery.activity.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v4.app.FragmentManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.commands.Command
import ru.vassuv.appgallery.App
import ru.vassuv.appgallery.R
import ru.vassuv.appgallery.fabric.FrmFabric
import ru.vassuv.appgallery.utils.atlibrary.BaseFragment
import ru.vassuv.appgallery.utils.routing.Navigator
import ru.vassuv.appgallery.utils.routing.Router
import ru.vassuv.appgallery.utils.UiListener
import java.io.Serializable

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    private var navigator: Navigator? = null
    private var fragmentManager: FragmentManager? = null
    private var currentType = FrmFabric.EMPTY

    private val changedFragmentListener = { setScreenState() }

    private val currentFragment: BaseFragment?
        get() = fragmentManager?.findFragmentById(R.id.fragment_container) as BaseFragment?

    fun onCreate(fragmentManager: FragmentManager, savedInstanceState: Bundle?, uiListener: UiListener) {
        this.fragmentManager = fragmentManager

        initRouter(uiListener)

        navigator = createNavigator(fragmentManager)
        App.setNavigationHolder(navigator)

        if (savedInstanceState == null) {
            onRunApplication()
        } else {
            navigator?.setScreenNames(savedInstanceState.getSerializable(STATE_SCREEN_NAMES) as ArrayList<*>)
        }

    }

    private fun onRunApplication() {
        Router.newRootScreen(FrmFabric.SPLASH.name)
    }

    private fun initRouter(uiListener: UiListener) {
        Router.onNewRootScreenListener = { screenKey ->
        }

        Router.onBackScreenListener = { }

        Router.uiListener = uiListener
    }

    private fun createNavigator(fragmentManager: FragmentManager): Navigator {
        return object : Navigator(fragmentManager, R.id.fragment_container, changedFragmentListener) {

            override fun createFragment(screenKey: String, data: Bundle) = FrmFabric.valueOf(screenKey).create(data)

            override fun exit() {
            }

            override fun openFragment(position: Int, name: String) {
                if (position == 0) viewState.hideBackButton() else viewState.showBackButton()
            }
        }
    }

    val onNavigationItemSelectedListener
        get() = OnNavigationItemSelectedListener { item ->

            fun startScreen(name: String): Boolean {
                if(name != currentType.name)
                    Router.newRootScreen(name)
                return true
            }

            when (item.itemId) {
//                R.id.navigation_home -> startScreen(FrmFabric.START.name)
//                R.id.navigation_dashboard -> startScreen(FrmFabric.SPLASH.name)
//                R.id.navigation_notifications -> startScreen(FrmFabric.INTRO.name)
                else -> false
            }
        }

    fun onStart() {
        setScreenState()
    }

    fun onResume() {
        App.setNavigationHolder(navigator)
    }

    fun onPause() {
        App.resetNavigator()
    }

    private fun setScreenState() {
        val fragment: BaseFragment = currentFragment ?: return
        val newType = FrmFabric.valueOf(fragment::class) ?: return

        if (currentType == newType) return

        currentType = newType

        viewState.setTitle(currentType.name)
        when (currentType) {
            FrmFabric.SPLASH -> {
                viewState.hideBottomNavigatorView()
                viewState.hideActionBar()
            }
            else -> {
                viewState.hideBottomNavigatorView()
                viewState.hideActionBar()
                viewState.hideBackButton()

//                viewState.changeTab(R.id.navigation_dashboard)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentManager = null
    }

    fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(STATE_SCREEN_NAMES, navigator?.screenNames as Serializable?)
    }

    companion object {
        private val STATE_SCREEN_NAMES = "state_screen_names"
    }
}
