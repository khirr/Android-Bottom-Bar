package net.khirr.library.bottombar

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

typealias OnItemClickListener = (Int) -> Boolean
class BottomBar(private val context: Activity, bottomBarView: BottomBarView) {

    class Item(val id: Int, val title: String, val icon: Int)
    private class BarViewItem(val view: View,
                              val container: RelativeLayout,
                              val badgeIndicator: View,
                              val subItemsContainer: LinearLayout,
                              val icon: ImageView,
                              val title: TextView)
    private class BarItem(val view: BarViewItem, val item: Item)

    private val barItems = ArrayList<BarItem>()
    private var onItemClickListener: OnItemClickListener? = null

    private var selectedColor = Color.parseColor("#FF4081")
    private var unselectedColor = Color.parseColor("#757575")
    private var badgeColor = Color.parseColor("#FF4081")

    private var selectedId: Int = -1

    var bottomBar = bottomBarView.findViewById(R.id.bottomBar) as RelativeLayout
    private var bottomBarItemsContainer: LinearLayout

    init {
        bottomBarItemsContainer = bottomBar.findViewById(R.id.bottomBarItemsContainer) as LinearLayout
        bottomBarItemsContainer.removeAllViews()
    }

    fun setOnItemClickListener(listener: OnItemClickListener): BottomBar {
        onItemClickListener = listener
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

        //  Default values
        title.text = item.title
        icon.setImageResource(item.icon)

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

    fun setBadgeIndicator(id: Int) {
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

}