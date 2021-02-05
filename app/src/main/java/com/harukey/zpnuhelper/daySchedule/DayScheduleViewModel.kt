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

package com.harukey.zpnuhelper.daySchedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.harukey.zpnuhelper.daySchedule.timeSchedule.DayScheduleTimeProvider
import com.harukey.zpnuhelper.daySchedule.timeSchedule.ScheduleStatus
import org.threeten.bp.format.DateTimeFormatter


class DayScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val _scheduleStatus = MutableLiveData<ScheduleStatus>()
    val scheduleStatus: LiveData<ScheduleStatus>
        get() = _scheduleStatus

    private val _timeLeft = MutableLiveData<String>()
    val timeLeft: LiveData<String>
        get() = _timeLeft

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    init {
        updateTimeStatus()
    }

    fun updateTimeStatus() {
        _scheduleStatus.value = DayScheduleTimeProvider.classStatus
        _timeLeft.value = when (DayScheduleTimeProvider.classStatus) {
            is ScheduleStatus.ClassesOver -> ""
            else -> timeFormatter.format(DayScheduleTimeProvider.classStatus.timeLeft)
        }
    }
}
