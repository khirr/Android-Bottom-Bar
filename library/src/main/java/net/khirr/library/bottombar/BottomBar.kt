package net.khirr.library.bottombar

import android.app.Activity
import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import q.rorbin.badgeview.QBadgeView

typealias OnItemClickListener = (Int) -> Boolean
class BottomBar(private val context: Activity, bottomBarView: BottomBarView) {

    class Item {
        var id: Int
        var title = ""
        var icon: Int

        constructor(id: Int, title: String, icon: Int) {
            this.id = id
            this.title = title
            this.icon = icon
        }

        constructor(id: Int, icon: Int) {
            this.id = id
            this.icon = icon
        }
    }

    private class BarViewItem(val view: View,
                              val container: RelativeLayout,
                              val badgeIndicator: View,
                              val subItemsContainer: LinearLayout,
                              val icon: ImageView,
                              val title: TextView) {
        var badgeCountView: QBadgeView? = null
    }

    private class BarItem(val view: BarViewItem, val item: Item)

    private val barItems = ArrayList<BarItem>()
    private var onItemClickListener: OnItemClickListener? = null
    private var enableBottomDivider = false
    private var badgeIndicatorSize = dpToPx(8)
    private var bottomBarColors = BottomBarColors()

    var selectedId: Int = -1

    var bottomBar = bottomBarView.findViewById(R.id.bottomBar) as RelativeLayout
    private var bottomBarItemsContainer: LinearLayout
    private var bottomBarDivider: View
    private var bottomBarDividerBottom: View

    init {
        bottomBarItemsContainer = bottomBar.findViewById(R.id.bottomBarItemsContainer) as LinearLayout
        bottomBarDivider = bottomBar.findViewById(R.id.bottomBarDivider)
        bottomBarDividerBottom = bottomBar.findViewById(R.id.bottomBarBottomDivider)
        bottomBarItemsContainer.removeAllViews()
    }

    fun setOnItemClickListener(listener: OnItemClickListener): BottomBar {
        onItemClickListener = listener
        return this
    }

    fun setBottomBarColors(bottomBarColors: BottomBarColors): BottomBar {
        this.bottomBarColors = bottomBarColors
        return this
    }

    fun enableBottomDivider(enable: Boolean): BottomBar {
        enableBottomDivider = enable
        if (enableBottomDivider) {
            bottomBarDividerBottom.visibility = View.VISIBLE
        } else {
            bottomBarDividerBottom.visibility = View.GONE
        }
        return this
    }

    fun setBadgeIndicatorSize(dp: Int): BottomBar {
        badgeIndicatorSize = dpToPx(dp)
        return this
    }

    fun forcePressed(id: Int) {
        setPressed(id)
        onItemClickListener?.invoke(id)
    }

    fun addItem(item: Item): BottomBar {
        val view = context.layoutInflater.inflate(R.layout.bottom_bar_item, bottomBarItemsContainer, false)
        val container = view.findViewById(R.id.bottomBarItemContainer) as RelativeLayout
        val badgeIndicator = view.findViewById(R.id.bottomBarBadgeIndicator) as View
        val subItemsContainer = view.findViewById(R.id.bottomBarSubItemsContainer) as LinearLayout
        val icon = view.findViewById(R.id.bottomBarItemIconImageView) as ImageView
        val title = view.findViewById(R.id.bottomBarItemTitleTextView) as TextView

        title.visibility = if (item.title.isEmpty()) View.GONE else View.VISIBLE

        //  Default values
        title.text = item.title
        icon.setImageResource(item.icon)

        setBackgroundColor(view, bottomBarColors.backgroundColor)

        val drawable = badgeIndicator.background as GradientDrawable
        drawable.setColor(bottomBarColors.badgeColor)

        // Badge indicator size
        badgeIndicator.layoutParams.width = badgeIndicatorSize
        badgeIndicator.layoutParams.height = badgeIndicatorSize

        //  Default colors
        title.setTextColor(bottomBarColors.unselectedColor)
        tint(icon, bottomBarColors.unselectedColor)

        val viewItem = BarViewItem(view, container, badgeIndicator, subItemsContainer, icon, title)
        val barItem = BarItem(viewItem, item)
        barItems.add(barItem)

        if (barItems.size == 1) {
            setPressed(barItem.item.id)
        }

        return this
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun setPressed(id: Int) {
        selectedId = id

        barItems.forEach { item ->
            item.view.title.setTextColor(bottomBarColors.unselectedColor)
            tint(item.view.icon, bottomBarColors.unselectedColor)
            if (item.item.id == id) {
                item.view.title.setTextColor(bottomBarColors.selectedColor)
                tint(item.view.icon, bottomBarColors.selectedColor)
            }
        }
    }

    fun build(): BottomBar {
        barItems.forEach { barItem ->
            bottomBarItemsContainer.addView(barItem.view.view)
            barItem.view.container.setOnClickListener {
                val pressed = onItemClickListener?.invoke(barItem.item.id)
                val result = (pressed != null && pressed)
                if (result || pressed == null)
                    setPressed(barItem.item.id)
            }
        }
        setBackgroundColor(bottomBar, bottomBarColors.backgroundColor)
        setBackgroundColor(bottomBarDivider, bottomBarColors.dividerColor)
        bottomBarDividerBottom.setBackgroundColor(bottomBarColors.bottomDividerColor)
        return this
    }

    fun setBadgeIndicator(id: Int, visible: Boolean) {
        barItems.forEach { barItem ->
            if (barItem.item.id == id) {
                barItem.view.badgeIndicator.visibility = if (visible) View.VISIBLE else View.GONE
                return
            }
        }
    }

    fun showBadgeIndicator(id: Int) {
        barItems.forEach { barItem ->
            if (barItem.item.id == id) {
                barItem.view.badgeIndicator.visibility = View.VISIBLE
                return
            }
        }
    }

    fun removeBadgeIndicator(id: Int) {
        barItems.forEach { barItem ->
            if (barItem.item.id == id) {
                barItem.view.badgeIndicator.visibility = View.GONE
                return
            }
        }
    }

    private fun getColor(color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    private fun tint(imageView: ImageView, color: Int) {
        imageView.setColorFilter(color)
    }

    fun onBackPressed(id: Int): Int {
        forcePressed(id)
        return id
    }

    fun setBadgeCount(id: Int, count: Int) {
        barItems.forEach { barItem ->
            if (barItem.item.id == id) {
                setBadge(barItem, count)
                return
            }
        }
    }

    private fun setBadge(barItem: BarItem, count: Int) {
        if (barItem.view.badgeCountView == null) {
            val badge = QBadgeView(context)
            badge.isShowShadow = false
            badge.stroke(bottomBarColors.badgeStrokeColor, 1.0f, true)
            badge.badgeBackgroundColor = bottomBarColors.badgeColor
            badge.bindTarget(barItem.view.subItemsContainer)
            barItem.view.badgeCountView = badge
        }

        if (count == 0) {
            barItem.view.badgeCountView?.hide(false)
        } else {
            if (count > 10)
                barItem.view.badgeCountView?.badgeText = "10+"
            else
                barItem.view.badgeCountView?.badgeNumber = count
        }

    }

    private fun setBackgroundColor(view: View?, resColor: Int) {
        if (view == null || view.background == null)
            return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && view.background is RippleDrawable) {
            val rippleDrawable = view.background as RippleDrawable
            rippleDrawable.setColorFilter(resColor, PorterDuff.Mode.MULTIPLY)
        } else {
            if (view.background is ColorDrawable) {
                //  Non ripple
                val drawable = view.background as ColorDrawable
                //  Stroke
                //drawable.setStroke(1, resColor);
                //  Solid
                drawable.color = resColor
            } else if (view.background is GradientDrawable) {
                //  Non ripple
                val drawable = view.background as GradientDrawable
                //  Stroke
                //drawable.setStroke(1, resColor);
                //  Solid
                drawable.setColor(resColor)
            }
        }
    }

}