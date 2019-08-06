package me.niccorder.phunware.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Our data model for a location.
 */
@Entity
data class Location(
  @PrimaryKey
  @SerializedName("zip_code")
  val zipCode: String,

  @SerializedName("city")
  val city: String,

  @SerializedName(value = "latitude", alternate = ["lat"])
  val latitude: Double,

  @SerializedName(value = "longitude", alternate = ["lng"])
  val longitude: Double

) {
  companion object {
    const val KEY_LOCATION = "key_location"
    val EMPTY = Location("-1", "", 0.0, 0.0)
  }
}