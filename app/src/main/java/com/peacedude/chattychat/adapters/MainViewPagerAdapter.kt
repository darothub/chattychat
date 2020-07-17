package com.peacedude.chattychat.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.peacedude.chattychat.ui.*

class MainViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> RequestFragment()
            1 -> ChatFragment()
            2 -> FriendsFragment()
            else -> LoginFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        super.getPageTitle(position)
        return when(position){
            0 -> "REQUESTS"
            1 -> "CHATS"
            2 -> "FRIENDS"
            else -> "NULL"
        }
    }
}