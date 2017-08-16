package net.khirr.library.bottombar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout

class BottomBarView : RelativeLayout {

    constructor(context: Context) : super(context) {
        inflateView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        inflateView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        inflateView()
    }

    private fun inflateView() {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.bottom_bar, this, true)
    }
}