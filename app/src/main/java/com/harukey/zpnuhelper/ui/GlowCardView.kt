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

package com.harukey.zpnuhelper.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.harukey.zpnuhelper.R


class GlowCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr) {

    private var _glowColor: Int
    var glowColor: Int
    get() = _glowColor
    set(value) {
        _glowColor = value
        updateBackgroundDrawable()
    }

    private var cornerRadius: Int

    private var _glowSize: Int
    var glowSize: Int
    get() = _glowSize
    set(value) {
        _glowSize = value
        updateBackgroundDrawable()
    }

    private var _cardBackgroundColor: Int
    var cardBackgroundColor: Int
    get() = _cardBackgroundColor
    set(value) {
        _cardBackgroundColor = value
        updateBackgroundDrawable()
    }

    private var _glowYOffset: Float = 0f

    private var _cardBorderSize: Int
    var cardBorderSize: Int
    get() = _cardBorderSize
    set(value) {
        _cardBorderSize = value
        updateBackgroundDrawable()
    }

    private var _cardBorderColor: Int
    var cardBorderColor: Int
        get() = _cardBorderColor
        set(value) {
            _cardBorderColor = value
            updateBackgroundDrawable()
        }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GlowCardView,
            0, 0).apply {
            try {
                _glowColor = getColor(
                    R.styleable.GlowCardView_glowColor,
                    ContextCompat.getColor(context, R.color.default_card_glow_color))
                cornerRadius = getDimensionPixelSize(
                    R.styleable.GlowCardView_cornerRadius,
                    resources.getDimensionPixelSize(R.dimen.default_glow_card_corner_radius))
                _glowSize = getDimensionPixelSize(
                    R.styleable.GlowCardView_glowSize,
                    resources.getDimensionPixelSize(R.dimen.default_glow_card_glow_size))
                _cardBackgroundColor = getColor(
                    R.styleable.GlowCardView_cardBackgroundColor,
                    ContextCompat.getColor(context, R.color.default_card_background))
                _glowYOffset = getFloat(
                    R.styleable.GlowCardView_glowYOffset,
                    0f)
                _cardBorderSize = getDimensionPixelSize(
                    R.styleable.GlowCardView_borderSize,
                    0)
                _cardBorderColor = getColor(
                    R.styleable.GlowCardView_borderColor,
                    ContextCompat.getColor(context, R.color.colorPrimary))
            } finally {
                recycle()
            }
        }
        setPadding(paddingLeft+_glowSize, paddingTop+_glowSize,
            paddingRight+_glowSize, paddingBottom+_glowSize)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        updateBackgroundDrawable()
    }

    private fun updateBackgroundDrawable() {
        background = getBackgroundWithGlow()
    }

    private fun getBackgroundWithGlow(
    ): Drawable {
        val outerRadius = FloatArray(8)
        outerRadius.fill(cornerRadius.toFloat())
        val shapeDrawablePadding = Rect()
        val shapeDrawable = ShapeDrawable()
        shapeDrawable.setPadding(shapeDrawablePadding)
        shapeDrawable.paint.color = _cardBackgroundColor
        if (!isUsingNightModeResources())
            shapeDrawable.paint.setShadowLayer(_glowSize.toFloat(), 0f, _glowYOffset, _glowColor)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            setLayerType(LAYER_TYPE_SOFTWARE, shapeDrawable.paint)
        }

        shapeDrawable.shape = RoundRectShape(outerRadius, null, null)
        val drawable: LayerDrawable?
        val borderDrawable = ShapeDrawable()
        if (_cardBorderSize != 0) {
            borderDrawable.paint.color = _cardBorderColor
            borderDrawable.setPadding(shapeDrawablePadding)
            borderDrawable.paint.style = Paint.Style.STROKE
            borderDrawable.paint.strokeWidth = _cardBorderSize.toFloat()
            borderDrawable.shape = RoundRectShape(outerRadius, null, null)

            drawable = LayerDrawable(arrayOf<Drawable>(shapeDrawable, borderDrawable))
            drawable.setLayerInset(
                1,
                _glowSize,
                _glowSize,
                _glowSize,
                _glowSize
            )
        } else {
            drawable = LayerDrawable(arrayOf<Drawable>(shapeDrawable))
        }

        drawable.setLayerInset(
            0,
            _glowSize,
            _glowSize,
            _glowSize,
            _glowSize
        )
        return drawable
    }

    private fun isUsingNightModeResources(): Boolean {
        return when (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

}