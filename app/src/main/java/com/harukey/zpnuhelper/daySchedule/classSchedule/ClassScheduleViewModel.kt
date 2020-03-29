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

package com.harukey.zpnuhelper.daySchedule.classSchedule

import android.app.Application
import androidx.lifecycle.*
import com.harukey.zpnuhelper.daySchedule.classSchedule.db.ClassScheduleItem
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.WeekFields

class ClassScheduleViewModel(application: Application) : AndroidViewModel(application) {
    private val classScheduleRepo: ClassScheduleRepo by lazy { ClassScheduleRepo(application) }

    private val _selectedClassScheduleItems = MutableLiveData<List<ClassScheduleItem>>()
    val selectedClassScheduleItems: LiveData<List<ClassScheduleItem>>
        get() = _selectedClassScheduleItems

    fun loadScheduleForDay(week: Int, day: Int) {
        viewModelScope.launch {
            _selectedClassScheduleItems.value = classScheduleRepo.getDaySchedule(week, day)
        }
    }

    fun updateScheduleEntry(entry: ClassScheduleItem) {
        viewModelScope.launch {
            classScheduleRepo.updateScheduleItem(entry)
        }
    }

    fun getCurrentWeekDay() : Int {
        val weekDay = LocalDate.now().dayOfWeek.ordinal
        return if (weekDay == 6) 0 else weekDay
    }

    fun getWeekType(): Int = if(LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear()) % 2 == 0) 1 else 0

}