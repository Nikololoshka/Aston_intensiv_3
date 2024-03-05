package dev.aston.intensiv.nikolay.ui

import android.util.TypedValue
import android.view.View

fun View.hide()  {
    visibility = View.GONE
}

fun View.changeVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.setRippleEffect() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
    setBackgroundResource(resourceId)
}