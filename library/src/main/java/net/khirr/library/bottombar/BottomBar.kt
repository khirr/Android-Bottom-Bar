package net.khirr.library.bottombar

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
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

    private var backgroundColor = Color.parseColor("#FFFFFF")
    private var selectedColor = Color.parseColor("#FF4081")
    private var unselectedColor = Color.parseColor("#757575")
    private var badgeColor = Color.parseColor("#FF4081")
    private var dividerColor = Color.parseColor("#dcdcdc")
    private var badgeStrokeColor = Color.parseColor("#FFFFFF")
    private var bottomDividerColor = Color.parseColor("#FF4081")
    private var enableBottomDivider = false

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

    fun setBackgroundColor(color: Int): BottomBar {
        backgroundColor = getColor(color)
        setBackgroundColor(bottomBar, backgroundColor)
        return this
    }

    fun setSelectedColor(color: Int): BottomBar {
        selectedColor = getColor(color)
        return this
    }

    fun setUnselectedColor(color: Int): BottomBar {
        unselectedColor = getColor(color)
        return this
    }

    fun setBadgeColor(color: Int): BottomBar {
        badgeColor = getColor(color)
        return this
    }

    fun setDividerColor(color: Int): BottomBar {
        dividerColor = getColor(color)
        setBackgroundColor(bottomBarDivider, dividerColor)
        return this
    }

    fun setBadgeStrokeColor(color: Int): BottomBar {
        badgeStrokeColor = getColor(color)
        return this
    }

    fun setBottomDividerColor(color: Int): BottomBar {
        bottomDividerColor = getColor(color)
        bottomBarDividerBottom.setBackgroundColor(bottomDividerColor)
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

        setBackgroundColor(view, backgroundColor)

        val drawable = badgeIndicator.background as GradientDrawable
        drawable.setColor(badgeColor)

        //  Default colors
        title.setTextColor(unselectedColor)
        tint(icon, unselectedColor)

        val viewItem = BarViewItem(view, container, badgeIndicator, subItemsContainer, icon, title)
        val barItem = BarItem(viewItem, item)
        barItems.add(barItem)

        if (barItems.size == 1)
            setPressed(barItem.item.id)

        return this
    }

    private fun setPressed(id: Int) {
        selectedId = id

        barItems.forEach { item ->
            item.view.title.setTextColor(unselectedColor)
            tint(item.view.icon, unselectedColor)
            if (item.item.id == id) {
                item.view.title.setTextColor(selectedColor)
                tint(item.view.icon, selectedColor)
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
            badge.stroke(badgeStrokeColor, 1.0f, true)
            badge.badgeBackgroundColor = badgeColor
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