package ru.vassuv.appgallery.utils

interface UiListener {
    fun showMessage(message: String)
    fun showLoader()
    fun hideLoader()
}