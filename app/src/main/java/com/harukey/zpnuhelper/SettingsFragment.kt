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

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.play.core.review.ReviewManagerFactory
import com.harukey.zpnuhelper.databinding.SettingsFragmentBinding


class SettingsFragment : Fragment() {

    private val sharedPref: SharedPreferences by lazy { requireContext().getSharedPreferences(APP_MAIN_PREFS, Context.MODE_PRIVATE) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = SettingsFragmentBinding.inflate(inflater)

        setupCustomSpinner(view.themeSpinner)
        restorePreferenceState(view.dateSyncCheckBox, view.themeSpinner)
        setSaveStateCallbacks(view.dateSyncCheckBox, view.themeSpinner)
        setButtonsCallbacks(view.developerButton, view.designerButton)

        setupReviewPopup()

        return view.root
    }

    private fun setupReviewPopup() {
        try {
            val manager = ReviewManagerFactory.create(context)
            manager.requestReviewFlow().addOnCompleteListener { request ->
                val counterValue = sharedPref.getInt(SETTINGS_COUNTER, 0)
                if (request.isSuccessful && counterValue == 1) {
                    val reviewInfo = request.result
                    manager.launchReviewFlow(activity, reviewInfo).addOnSuccessListener {
                        incrementCounter()
                    }
                } else if (counterValue > 3) {
                    resetCounter()
                } else if (request.isSuccessful) {
                    incrementCounter()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupCustomSpinner(spinner: Spinner) {
        val adapter = object : ArrayAdapter<Any>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.theme_mode)) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                // Highlight selected item in spinner dropdown
                return super.getDropDownView(position, convertView, parent).also { view ->
                    if (position == spinner.selectedItemPosition) {
                        view.setBackgroundColor(
                            ContextCompat.getColor(context, R.color.selectedElementColor)
                        )
                        (view as TextView).setTextColor(
                            ContextCompat.getColor(context, R.color.colorPrimary)
                        )
                    }

                }
            }

            // Remove spinner label to leave only arrow button
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val spinnerLabel = super.getView(position, convertView, parent)
                spinnerLabel.visibility = View.GONE
                return spinnerLabel
            }
        }

        spinner.adapter = adapter
    }

    private fun restorePreferenceState(checkBox: CheckBox, spinner: Spinner) {
        checkBox.isChecked = sharedPref.getBoolean(SYNC_DATE_PREFERENCE, false)

        val themeSelection = when(sharedPref.getInt(THEME_PREFERENCE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> 0
            AppCompatDelegate.MODE_NIGHT_NO -> 1
            AppCompatDelegate.MODE_NIGHT_YES -> 2
            else -> 0
        }
        spinner.setSelection(themeSelection)
    }

    private fun setSaveStateCallbacks(checkBox: CheckBox, spinner: Spinner) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            with(sharedPref.edit()) {
                putBoolean(SYNC_DATE_PREFERENCE, isChecked)
                apply()
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position) {
                    0 -> {
                        saveThemePreference(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        AppCompatDelegate
                            .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                    1 -> {
                        saveThemePreference(AppCompatDelegate.MODE_NIGHT_NO)
                        AppCompatDelegate
                            .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    2 -> {
                        saveThemePreference(AppCompatDelegate.MODE_NIGHT_YES)
                        AppCompatDelegate
                            .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
            }

        }
    }

    private fun saveThemePreference(preference: Int) {
        with(sharedPref.edit()) {
            putInt(THEME_PREFERENCE, preference)
            apply()
        }
    }

    private fun setButtonsCallbacks(developerButton: Button, designerButton: Button) {
        developerButton.setOnClickListener {
            val linkIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/HarukeyUA"))
            linkIntent.resolveActivity(requireActivity().packageManager)?.let {
                startActivity(linkIntent)
            }
        }
        designerButton.setOnClickListener {
            val linkIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/atimohov"))
            linkIntent.resolveActivity(requireActivity().packageManager)?.let {
                startActivity(linkIntent)
            }
        }
    }

    private fun incrementCounter() {
        val currentValue = sharedPref.getInt(SETTINGS_COUNTER, 0)
        with(sharedPref.edit()) {
            putInt(SETTINGS_COUNTER, currentValue.plus(1))
            apply()
        }
    }

    private fun resetCounter() {
        with(sharedPref.edit()) {
            putInt(SETTINGS_COUNTER, 0)
            apply()
        }
    }

}