package me.niccorder.phunware.shared

import android.content.Context

sealed class AppActivity

/**
 * TODO(niccorder) – Figure out a cleaner way to go about navigation
 */
interface Navigator {
  fun toWeather(from: Context, zipCode: String)
}