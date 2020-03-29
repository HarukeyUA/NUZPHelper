/*
 * Copyright 2020 Nazar Rusnak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.harukey.zpnuhelper.daySchedule

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.harukey.zpnuhelper.R
import com.harukey.zpnuhelper.databinding.DayScheduleFragmentBinding
import com.harukey.zpnuhelper.daySchedule.classSchedule.ClassScheduleFragment
import com.harukey.zpnuhelper.daySchedule.timeSchedule.TimeScheduleFragment


class DayScheduleFragment : Fragment() {

    private val viewModel: DayScheduleViewModel by viewModels()

    val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<DayScheduleFragmentBinding>(inflater, R.layout.day_schedule_fragment, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        binding.pager.adapter = PagerAdapter(this)


        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, binding.pager) { tab, position ->
            if (position == 0) {
                tab.text = getString(R.string.day_schedule)
            } else if (position == 1) {
                tab.text = getString(R.string.class_schedule)
            }
        }.attach()

        binding.pager.offscreenPageLimit = 1

        binding.pager.registerOnPageChangeCallback(object:
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position != 0)
                    binding.timeLeftCard.visibility = View.INVISIBLE
                else
                    binding.timeLeftCard.visibility = View.VISIBLE
            }
        })

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateClassesInfoTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateClassesInfoTask)
    }

    private val updateClassesInfoTask = object : Runnable {
        override fun run() {
            viewModel.updateTimeStatus()
            mainHandler.postDelayed(this, 1000)
        }
    }

}

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) TimeScheduleFragment() else ClassScheduleFragment()
    }


}
