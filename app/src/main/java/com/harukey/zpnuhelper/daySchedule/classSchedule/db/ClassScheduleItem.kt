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

import androidx.room.Entity

@Entity(tableName = "class_schedule_items", primaryKeys = ["Time", "Week", "DayOfWeek"])
data class ClassScheduleItem(
    var ClassName: String = "", var ClassNum: String = "",
    var Time: Int = 0, var Week: Int = 0, var DayOfWeek: Int = 0
)