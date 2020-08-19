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

package com.harukey.zpnuhelper.daySchedule.timeSchedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class TimeScheduleViewModel : ViewModel() {

    private val _scheduleStatus = MutableLiveData<ScheduleStatus>()

    val classIndicationIndex = Transformations.map(_scheduleStatus) { status ->
        when(status) {
            is ScheduleStatus.CurrentClass -> status.currentClass
            is ScheduleStatus.Break -> status.upcomingClass
            ScheduleStatus.ClassesOver -> -1
        }}

    val isBreak = Transformations.map(_scheduleStatus) { status ->
        when(status) {
            is ScheduleStatus.Break -> true
            else -> false
        }
    }

    fun updateTimeStatus() {
        _scheduleStatus.value = DayScheduleTimeProvider.classStatus
    }
}