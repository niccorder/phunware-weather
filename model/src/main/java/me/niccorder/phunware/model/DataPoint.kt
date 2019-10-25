package me.niccorder.phunware.model

import com.google.gson.annotations.SerializedName

data class DataPoint(

  @SerializedName("icon")
  val forecastIcon: ForecastIcon?,

  @SerializedName("apparentTemperature")
  val appearantTemperature: Double?,

  @SerializedName("apparentTemperatureHigh")
  val apparentTemperatureHigh: Double?,

  @SerializedName("apparentTemperatureHighTime")
  val apparentTemperatureHighTime: Long?,

  @SerializedName("apparentTemperatureLow")
  val apparentTemperatureLow: Double?,

  @SerializedName("apparentTemperatureLowTime")
  val apparentTemperatureLowTime: Long?,

  @SerializedName("apparentTemperatureMax")
  val apparentTemperatureMax: Double?,

  @SerializedName("apparentTemperatureMaxTime")
  val apparentTemperatureMaxTime: Long?,

  @SerializedName("apparentTemperatureMin")
  val apparentTemperatureMin: Double?,

  @SerializedName("apparentTemperatureMinTime")
  val apparentTemperatureMinTime: Long?,

  @SerializedName("cloudCover")
  val cloudCover: Double?,

  @SerializedName("summary")
  val summary: String?,

  @SerializedName("sunriseTime")
  val sunriseTime: Long?,

  @SerializedName("sunsetTime")
  val sunsetTime: Long?,

  @SerializedName("temperature")
  val temperature: Double?,

  @SerializedName("temperatureHigh")
  val temperatureHigh: Double?,

  @SerializedName("temperatureHighTime")
  val temperatureHighTime: Long?,

  @SerializedName("temperatureLow")
  val temperatureLow: Double?,

  @SerializedName("temperatureLowTime")
  val temperatureLowTime: Long?,

  @SerializedName("temperatureMax")
  val temperatureMax: Double?,

  @SerializedName("temperatureMaxTime")
  val temperatureMaxTime: Long?,

  @SerializedName("temperatureMin")
  val temperatureMin: Double?,

  @SerializedName("temperatureMinTime")
  val temperatureMinTime: Long?,

  @SerializedName("time")
  val time: Long,

  @SerializedName("humidity")
  val humidity: Double?
)