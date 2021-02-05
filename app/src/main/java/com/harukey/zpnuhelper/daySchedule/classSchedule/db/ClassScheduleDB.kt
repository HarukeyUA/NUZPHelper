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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ClassScheduleItem::class], version = 1, exportSchema = false)
abstract class ClassScheduleDB : RoomDatabase() {

    abstract fun scheduleDao(): ClassScheduleDAO

    companion object {
        @Volatile
        private var INSTANCE: ClassScheduleDB? = null

        fun getDatabase(context: Context): ClassScheduleDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClassScheduleDB::class.java,
                    "class_schedule_database"
                ).createFromAsset("class_schedule_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}