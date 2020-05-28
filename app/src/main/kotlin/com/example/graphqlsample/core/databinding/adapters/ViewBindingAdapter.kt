package com.example.graphqlsample.core.databinding.adapters

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.databinding.BindingAdapter
import com.example.graphqlsample.R

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean?) {
    val firstTimeSet = getTag(R.id.visibility_set) != true
    setTag(R.id.visibility_set, true)
    if (visible != null && visible) {
        if (visibility == View.VISIBLE) return
        visibility = View.VISIBLE
        if (firstTimeSet) {
            alpha = 1F
        } else {
            alpha = 0F
            animate()
                .alpha(1F)
                .setListener(null)
        }
    } else {
        if (visibility != View.VISIBLE) return
        if (firstTimeSet) {
            visibility = View.GONE
        } else {
            animate()
                .alpha(0F)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        visibility = View.GONE
                    }
                })
        }
    }
}
