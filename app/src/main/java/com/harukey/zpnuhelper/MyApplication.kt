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

package com.harukey.zpnuhelper

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.jakewharton.threetenabp.AndroidThreeTen


@Suppress("unused")
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences =
            getSharedPreferences(APP_MAIN_PREFS, Context.MODE_PRIVATE)
        AppCompatDelegate.setDefaultNightMode(sharedPreferences.getInt(THEME_PREFERENCE,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM))

        AndroidThreeTen.init(this)
    }
}