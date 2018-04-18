package ru.vassuv.appgallery.utils.routing.animate

import ru.terrakok.cicerone.commands.Command
import android.support.v4.app.FragmentTransaction

class AnimateForward(
        val screenKey: String,
        val transitionData: Any?,
        val animate: FragmentTransaction.() -> Unit
) : Command