package ru.vassuv.appgallery.activity.main

import com.arellomobile.mvp.MvpView

interface MainView : MvpView {
    fun changeTab(position: Int)
    fun setTitle(title: String)
    fun hideBottomNavigatorView()
    fun showBottomNavigatorView()
    fun hideBackButton()
    fun showBackButton()
    fun hideActionBar()
    fun showActionBar()
}
