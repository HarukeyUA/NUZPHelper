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

import android.content.Context
import com.harukey.zpnuhelper.daySchedule.classSchedule.db.ClassScheduleDB
import com.harukey.zpnuhelper.daySchedule.classSchedule.db.ClassScheduleItem

class ClassScheduleRepo(context: Context) {

    private var database: ClassScheduleDB = ClassScheduleDB.getDatabase(context)

    suspend fun getDaySchedule(week: Int, day: Int): List<ClassScheduleItem> {
        return database.scheduleDao().getDayItems(week, day)
    }

    suspend fun updateScheduleItem(item: ClassScheduleItem) {
        database.scheduleDao().updateScheduleItems(item)
    }
}