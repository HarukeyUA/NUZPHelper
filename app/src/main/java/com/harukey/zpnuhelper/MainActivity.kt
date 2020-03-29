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

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navHostController = supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNavigationView, navHostController.navController)

        // Hide bottom navigation when keyboard is open on schedule fragment
        setEventListener(
            this,
            object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                    if (navHostController.navController.currentDestination?.id == R.id.dayScheduleFragment) {
                        if (isOpen) {
                            bottomNavigationView.animate()
                                .translationY(bottomNavigationView.height.toFloat())
                                .setDuration(150)
                                .withEndAction { bottomNavigationView.visibility = View.GONE }
                                .start()
                        } else {
                            bottomNavigationView.animate().translationY(0f)
                                .setDuration(150)
                                .setInterpolator(AccelerateDecelerateInterpolator())
                                .withStartAction { bottomNavigationView.visibility = View.VISIBLE }
                                .setStartDelay(20).start()
                        }
                    }
                }
            })
    }
}
