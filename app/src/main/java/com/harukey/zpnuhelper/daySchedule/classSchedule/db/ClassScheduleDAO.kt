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

package com.harukey.zpnuhelper.daySchedule.classSchedule.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update

@Dao
interface ClassScheduleDAO {

    @Update
    suspend fun updateScheduleItems(vararg item: ClassScheduleItem)

    @Query("SELECT * from class_schedule_items where Week = :week and DayOfWeek = :day ORDER BY DayOfWeek ASC")
    suspend fun getDayItems(week: Int, day: Int): List<ClassScheduleItem>
}