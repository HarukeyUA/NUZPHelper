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

package com.harukey.zpnuhelper.phoneBook

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.harukey.zpnuhelper.R


@BindingAdapter("hideIfEmpty")
fun View.hideIfEmpty(item: String) {
    if (item == "")
        this.visibility = View.GONE
    else
        this.visibility = View.VISIBLE
}

@BindingAdapter("stringToFormat", "argument")
fun TextView.bindInfoEntry(stringToFormat: String, argument: String) {
    val fullString = "$stringToFormat $argument"
    val typefaceSpan = TypefaceSpan("sans-serif-medium")
    val spannable = SpannableString(fullString)
    spannable.setSpan(
        typefaceSpan,
        stringToFormat.length,
        fullString.length,
        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
    )
    spannable.setSpan(
        ForegroundColorSpan(
            ContextCompat.getColor(
                context,
                R.color.textPrimaryColor
            )
        ), stringToFormat.length, fullString.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
    )
    this.text = spannable

}
