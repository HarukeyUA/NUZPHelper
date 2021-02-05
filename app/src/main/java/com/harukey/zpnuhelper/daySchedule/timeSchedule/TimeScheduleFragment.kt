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

package com.harukey.zpnuhelper.daySchedule.timeSchedule

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.harukey.zpnuhelper.databinding.TimeScheduleFragmentBinding

class TimeScheduleFragment : Fragment() {

    private val viewModel: TimeScheduleViewModel by viewModels()

    val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = TimeScheduleFragmentBinding.inflate(inflater)
        val cardsList = view.timeCards
        cardsList.setHasFixedSize(true)
        cardsList.adapter = ClassTimeAdapter(this, viewModel)
        viewModel.updateTimeStatus()
        return view.root
    }

    private val updateClassesInfoTask = object : Runnable {
        override fun run() {
            viewModel.updateTimeStatus()
            mainHandler.postDelayed(this, 1000)
        }
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateClassesInfoTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateClassesInfoTask)
    }

}