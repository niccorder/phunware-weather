package me.niccorder.phunware.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Our data model for a location.
 */
@Entity
data class Location(
        @PrimaryKey @SerializedName("zip_code") val zipCode: String,
        @SerializedName("city") val city: String,
        @SerializedName(value = "latitude", alternate = arrayOf("lat")) val latitude: Double,
        @SerializedName(value = "longitude", alternate = arrayOf("lng")) val longitude: Double
) {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Location) return false
        return zipCode == other.zipCode
    }

    companion object {
        const val KEY_LOCATION = "key_location"
        val EMPTY = Location("-1", "", 0.0, 0.0)
    }
}