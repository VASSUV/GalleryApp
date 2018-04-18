package ru.vassuv.appgallery.utils.routing.animate

import android.support.transition.ChangeBounds
import android.support.transition.ChangeTransform
import android.support.transition.TransitionSet

class DetailsTransition : TransitionSet() {
    init {
        ordering = ORDERING_TOGETHER
        addTransition(ChangeBounds())
                .addTransition(ChangeTransform())
//                .addTransition(ChangeImageTransform())
                .setDuration(400)
                .setStartDelay(500)
    }
}