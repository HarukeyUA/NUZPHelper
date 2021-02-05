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

package com.harukey.zpnuhelper.daySchedule.timeSchedule

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.harukey.zpnuhelper.R
import com.harukey.zpnuhelper.ui.GlowCardView

@BindingAdapter("classIndex")
fun TextView.bindClassText(classIndex: Int?) {
    classIndex?.let {
        text = when (classIndex) {
            0 -> resources.getString(R.string.first_class)
            1 -> resources.getString(R.string.second_class)
            2 -> resources.getString(R.string.third_class)
            3 -> resources.getString(R.string.fourth_class)
            4 -> resources.getString(R.string.fifth_class)
            5 -> resources.getString(R.string.sixth_class)
            6 -> resources.getString(R.string.seventh_class)
            7 -> resources.getString(R.string.eighth_class)
            else -> ""
        }
    }
}

@BindingAdapter("isCurrentClass", "isBreak")
fun ImageView.bindClassIcon(isCurrentClass: Boolean, isBreak: Boolean) {
    if (isCurrentClass)
        if (isBreak) {
            setImageResource(R.drawable.ic_clock)
            setColorFilter(ContextCompat.getColor(context, R.color.accent))
        } else {
            setImageResource(R.drawable.ic_check)
            setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
        }
    else
        setImageDrawable(null)
}

@BindingAdapter("isCurrentClass", "isBreak")
fun TextView.bindClassText(isCurrentClass: Boolean, isBreak: Boolean) {
    if (isCurrentClass) {
        if (isBreak)
            setTextColor(ContextCompat.getColor(context, R.color.accent))
        else
            setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
    } else {
        setTextColor(ContextCompat.getColor(context, R.color.inactiveElementColor))
    }
}


@BindingAdapter("isCurrentClass", "isBreak")
fun GlowCardView.setCardStatus(isCurrentClass: Boolean, isBreak: Boolean) {
    if (isCurrentClass) {
        cardBorderColor = if (isBreak)
            ContextCompat.getColor(context, R.color.accent)
        else
            ContextCompat.getColor(context, R.color.colorPrimary)
        cardBorderSize = resources.getDimensionPixelSize(R.dimen.card_border_radius)
    } else {
        cardBorderSize = 0
    }
}

