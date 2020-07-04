package adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import android.util.SparseArray





class SectionStatePagerAdapter(fragmentManager: FragmentManager)  : FragmentStatePagerAdapter(fragmentManager) {


    public var FragmentList = mutableListOf<Fragment>()
    public var FragmentTitleList = mutableListOf<String>()


    fun addFragment(fragment : Fragment , title : String)
    {
        FragmentList.add(fragment)
        FragmentTitleList.add(title)
    }

    override fun getCount(): Int {
        return FragmentList.size
    }

    override fun getItem(p0: Int): Fragment {
        return FragmentList[p0]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return FragmentTitleList[position]
    }
}