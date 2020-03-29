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

import org.threeten.bp.LocalTime

const val CLASSES_NUM = 8

sealed class ScheduleStatus(val timeLeft: LocalTime) {
    class CurrentClass(timeLeft: LocalTime, val currentClass: Int): ScheduleStatus(timeLeft)
    class Break(timeLeft: LocalTime, val upcomingClass: Int): ScheduleStatus(timeLeft)
    object ClassesOver: ScheduleStatus(LocalTime.of(0, 0))
}

object DayScheduleTimeProvider {

    private val mStartClassesTime: List<LocalTime> = listOf(LocalTime.of(8, 30),
        LocalTime.of(10, 5), LocalTime.of(11, 55),
        LocalTime.of(13, 25), LocalTime.of(14, 55),
        LocalTime.of(16, 45), LocalTime.of(18, 15),
        LocalTime.of(19, 45))

    private val mEndClassesTime: List<LocalTime> = listOf(LocalTime.of(9, 50),
        LocalTime.of(11, 25), LocalTime.of(13, 15),
        LocalTime.of(14, 45), LocalTime.of(16, 15),
        LocalTime.of(18, 5), LocalTime.of(19, 35),
        LocalTime.of(21, 5))


    val classStatus: ScheduleStatus
        get() {
            val currentTime = LocalTime.now()
            if (currentTime.isAfter(mEndClassesTime[CLASSES_NUM-1]))
                return ScheduleStatus.ClassesOver
            if (currentTime.isBefore(mStartClassesTime[0]))
                return ScheduleStatus.Break(calculateBreakTimeLeft(0), 0)

            for (i in 0 until CLASSES_NUM) {
                if (isTimeInInterval(mStartClassesTime[i], mEndClassesTime[i]))
                    return ScheduleStatus.CurrentClass(calculateClassTimeLeft(i), i)
                if (isTimeInInterval(mEndClassesTime[i], mStartClassesTime[i+1]))
                    return ScheduleStatus.Break(calculateBreakTimeLeft(i+1),
                        i+1)
            }

            return ScheduleStatus.ClassesOver
        }

    private fun isTimeInInterval(time1: LocalTime, time2: LocalTime): Boolean =
        LocalTime.now().isAfter(time1) && LocalTime.now().isBefore(time2)

    private fun calculateClassTimeLeft(classNum: Int): LocalTime =
        mEndClassesTime[classNum].minusNanos(LocalTime.now().toNanoOfDay())

    private fun calculateBreakTimeLeft(upcomingClassNum: Int): LocalTime =
        mStartClassesTime[upcomingClassNum].minusNanos(LocalTime.now().toNanoOfDay())

}