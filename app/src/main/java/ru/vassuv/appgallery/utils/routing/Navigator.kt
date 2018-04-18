package ru.vassuv.appgallery.utils.routing

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.commands.*
import ru.vassuv.appgallery.utils.atlibrary.Logger
import ru.vassuv.appgallery.utils.routing.animate.AnimateForward

abstract class Navigator
protected constructor(private val fragmentManager: FragmentManager,
                      private val containerId: Int,
                      private var onChangeFragment: () -> Unit) : Navigator {
    internal var screenNames: MutableList<String> = ArrayList()

    init {
        fragmentManager.addOnBackStackChangedListener { onChangeFragment() }
    }

    private fun FragmentTransaction.applyCommands(commands: Array<out Command>?): FragmentTransaction {
        commands?.forEach { command ->
            when (command) {
                is Forward -> {
                    replace(containerId, createFragment(command.screenKey, command.transitionData as Bundle))
                    addToBackStack(command.screenKey)
                    screenNames.add(command.screenKey)
                }
                is AnimateForward -> {
                    replace(containerId, createFragment(command.screenKey, command.transitionData as Bundle))
                    apply {
                        command.animate.invoke(this)
                    }
                    addToBackStack(command.screenKey)
                    screenNames.add(command.screenKey)
                }
                is Back -> {
                    if (fragmentManager.backStackEntryCount > 0)
                        fragmentManager.popBackStackImmediate()
                    else
                        exit()

                    if (screenNames.size > 0)
                        screenNames.removeAt(screenNames.size - 1)
                }
                is Replace -> {
                    if (fragmentManager.backStackEntryCount > 0) {
                        fragmentManager.popBackStackImmediate()
                    }
                    replace(containerId, createFragment(command.screenKey, command.transitionData as Bundle))
                    addToBackStack(command.screenKey)

                    if (screenNames.size > 0)
                        screenNames.removeAt(screenNames.size - 1)
                    screenNames.add(command.screenKey)
                }
                is BackTo -> {
                    val key = command.screenKey

                    if (key == null) {
                        backToRoot()
                        screenNames.clear()
                    } else {
                        var hasScreen = false
                        for (i in 0 until fragmentManager.backStackEntryCount) {
                            if (key == fragmentManager.getBackStackEntryAt(i).name) {
                                fragmentManager.popBackStackImmediate(key, 0)
                                hasScreen = true
                                break
                            }
                        }
                        if (!hasScreen) {
                            backToUnexisting()
                        }
                    }
                    if (screenNames.size > 0)
                        screenNames = ArrayList(screenNames.subList(0,
                                fragmentManager.backStackEntryCount + 1))
                }
            }
        }
        return this
    }

    override fun applyCommands(commands: Array<out Command>?) {
        synchronized(this) {
            fragmentManager
                    .beginTransaction()
                    .applyCommands(commands)
                    .commit()
//        fragmentManager.executePendingTransactions()
            val lastFragment = screenNames.lastOrNull()
            if (lastFragment != null) openFragment(screenNames.size - 1, lastFragment)
            printScreensScheme()
        }
    }

    private fun printScreensScheme() = Logger.trace("Screen chain:", screenNames.joinToString(" ➔ ", "[", "]"))

    fun setScreenNames(value: MutableList<*>) {
        screenNames = value.map { it.toString() } as MutableList<String>
        printScreensScheme()
    }

    private fun backToRoot() {
        (0 until fragmentManager.backStackEntryCount).forEach { fragmentManager.popBackStack() }
//        fragmentManager.executePendingTransactions()
    }

    protected abstract fun createFragment(screenKey: String, data: Bundle): Fragment?

    protected abstract fun exit()

    protected abstract fun openFragment(position: Int, name: String)

    private fun backToUnexisting() = backToRoot()

}
