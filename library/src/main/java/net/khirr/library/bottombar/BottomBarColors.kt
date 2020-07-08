package net.khirr.library.bottombar

import android.graphics.Color

open class BottomBarColors {
    internal var backgroundColor = Color.parseColor("#FFFFFF")
    internal var selectedColor = Color.parseColor("#FF4081")
    internal var unselectedColor = Color.parseColor("#757575")
    internal var badgeColor = Color.parseColor("#FF4081")
    internal var dividerColor = Color.parseColor("#dcdcdc")
    internal var badgeStrokeColor = Color.parseColor("#FFFFFF")
    internal var bottomDividerColor = Color.parseColor("#FF4081")

    fun setBackgroundColor(color: Int): BottomBarColors {
        backgroundColor = color
        return this
    }

    fun setSelectedColor(color: Int): BottomBarColors {
        selectedColor = color
        return this
    }

    fun setUnselectedColor(color: Int): BottomBarColors {
        unselectedColor = color
        return this
    }

    fun setBadgeColor(color: Int): BottomBarColors {
        badgeColor = color
        return this
    }

    fun setDividerColor(color: Int): BottomBarColors {
        dividerColor = color
        return this
    }

    fun setBadgeStrokeColor(color: Int): BottomBarColors {
        badgeStrokeColor = color
        return this
    }

    fun setBottomDividerColor(color: Int): BottomBarColors {
        bottomDividerColor = color
        return this
    }
}