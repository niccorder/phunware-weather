package me.niccorder.phunware.data.remote

import io.reactivex.Observable
import me.niccorder.phunware.model.Forecast
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * A [retrofit2.Retrofit] rest API definition for our weather services.
 */
interface WeatherApi {

  /**
   * Retrieves the forecast for the given latitude and longitude.
   */
  @GET("/forecast/{apiKey}/{latitude},{longitude}")
  fun getForcast(
    @Path("apiKey") apiKey: String,
    @Path("latitude") latitude: Double,
    @Path("longitude") longitude: Double
  ): Observable<Forecast>
}