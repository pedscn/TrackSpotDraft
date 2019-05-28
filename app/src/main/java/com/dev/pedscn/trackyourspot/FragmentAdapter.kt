import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.dev.pedscn.trackyourspot.BackBodySelectionFragment
import com.dev.pedscn.trackyourspot.FrontBodySelectionFragment

class FragmentAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? = when (position) {
        0 -> FrontBodySelectionFragment.newInstance()
        1 -> BackBodySelectionFragment.newInstance()
        else -> null
    }

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> "Tab 1 Item"
        1 -> "Tab 2 Item"
        else -> ""
    }

    override fun getCount(): Int = 2
}