package net.khirr.library.bottombar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.appcompat.app.AppCompatActivity

/**
 * @context Activity context
 * @resId FrameLayout resource id
 */
class MultipleFragmentsManager(private val context: AppCompatActivity, private val resId: Int) {

    private class Item(val fragment: Fragment, val tag: String) {
        var added = false
    }

    private val items = ArrayList<Item>()
    var currentFragment: Fragment? = null

    fun addItem(fragment: Fragment, tag: String): MultipleFragmentsManager {
        items.add(Item(fragment, tag))
        return this
    }

    @Synchronized
    fun select(tag: String) {
        val fragment = context.supportFragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            val transaction = context.supportFragmentManager.beginTransaction()
            transaction.show(fragment)
            hideOtherFragments(fragment, transaction)
            transaction.commitAllowingStateLoss()
            //  Set current fragment
            currentFragment = fragment
        } else {
            items.forEach { item ->
                if (item.tag == tag && !item.added) {
                    val transaction = context.supportFragmentManager.beginTransaction()
                    hideOtherFragments(item.fragment, transaction)
                    transaction.add(resId, item.fragment, item.tag)
                    transaction.commitAllowingStateLoss()
                    //  Set as Added, needed to avoid fragment already added
                    item.added = true
                    //  Set current fragment
                    currentFragment = item.fragment
                    return@forEach
                }
            }
        }
    }

    private fun hideOtherFragments(currentFragment: Fragment, fragmentTransaction: FragmentTransaction) {
        if (context.supportFragmentManager.fragments == null)
            return

        val fragmentsToHide = ArrayList<Fragment>()
        context.supportFragmentManager.fragments.forEach { fragment ->
            if (currentFragment.tag != fragment.tag)
                fragmentsToHide.add(fragment)
        }
        fragmentsToHide.forEach { fragment ->
            fragmentTransaction.hide(fragment)
        }
    }
}