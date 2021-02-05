/*
 * Copyright 2021 Nazar Rusnak
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

package com.harukey.zpnuhelper.daySchedule.classSchedule

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.harukey.zpnuhelper.APP_MAIN_PREFS
import com.harukey.zpnuhelper.SYNC_DATE_PREFERENCE
import com.harukey.zpnuhelper.databinding.UserScheduleFragmentBinding

class ClassScheduleFragment : Fragment() {

    private val viewModel: ClassScheduleViewModel by viewModels()

    private lateinit var spinnerWeekDay: Spinner
    private lateinit var spinnerWeekType: Spinner
    private lateinit var scheduleAdapter: ClassScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = UserScheduleFragmentBinding.inflate(inflater)

        val classScheduleList = view.scheduleList
        spinnerWeekDay = view.spinnerWeekDay
        spinnerWeekType = view.spinnerWeekType

        scheduleAdapter = ClassScheduleAdapter(viewModel)
        classScheduleList.adapter = scheduleAdapter

        viewModel.selectedClassScheduleItems.observe(viewLifecycleOwner, { list ->
            scheduleAdapter.submitList(list)
        })

        spinnerWeekDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                viewModel.loadScheduleForDay(getSelectedWeekType(), getSelectedWeekDay())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerWeekType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                viewModel.loadScheduleForDay(getSelectedWeekType(), getSelectedWeekDay())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        syncWithDate()

        return view.root
    }

    private fun getSelectedWeekType(): Int = spinnerWeekType.selectedItemPosition

    private fun getSelectedWeekDay(): Int = spinnerWeekDay.selectedItemPosition

    private fun syncWithDate() {
        val sharedPref = requireContext().getSharedPreferences(APP_MAIN_PREFS, Context.MODE_PRIVATE)
        sharedPref?.let {
            if (sharedPref.getBoolean(SYNC_DATE_PREFERENCE, false)) {
                spinnerWeekDay.setSelection(viewModel.getCurrentWeekDay())
                spinnerWeekType.setSelection(viewModel.getWeekType())
            }
        }
    }

}