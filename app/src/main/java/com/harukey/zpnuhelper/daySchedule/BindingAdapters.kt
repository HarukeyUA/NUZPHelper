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

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.harukey.zpnuhelper.R
import com.harukey.zpnuhelper.daySchedule.timeSchedule.ScheduleStatus

@BindingAdapter("scheduleStatus")
fun TextView.setClassStatus(scheduleStatus: ScheduleStatus) {
    text = when (scheduleStatus) {
        is ScheduleStatus.CurrentClass -> resources.getText(R.string.time_left_class)
        is ScheduleStatus.Break -> resources.getText(R.string.time_left_break)
        ScheduleStatus.ClassesOver -> resources.getText(R.string.classes_end)
    }
}
