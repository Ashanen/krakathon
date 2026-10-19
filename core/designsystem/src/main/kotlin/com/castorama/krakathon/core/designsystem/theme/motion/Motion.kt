package com.castorama.krakathon.core.designsystem.theme.motion

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Immutable

@Immutable
object Motion {
    object Duration {
        const val short: Int = 100
        const val medium: Int = 250
        const val long: Int = 450
    }

    object Easing {
        val standard = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
        val decelerate = CubicBezierEasing(0.0f, 0.0f, 0.0f, 1.0f)
        val accelerate = CubicBezierEasing(0.3f, 0.0f, 1.0f, 1.0f)
        val emphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
    }

    object Spring {
        val base: SpringSpec<Float> = spring(dampingRatio = 0.707f, stiffness = 200f)
        val press: SpringSpec<Float> = spring(dampingRatio = 0.433f, stiffness = 300f)
    }
}
