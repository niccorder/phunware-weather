package me.niccorder.phunware.data.remote

import me.niccorder.phunware.model.Location
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Our [retrofit2.Retrofit] REST api contract. Retrofit2 will generate the code necessary to allow
 * us to easily hit an endpoint.
 */
interface LocationApi {

  /**
   * Get's human readable information pertaining to the given zip code.
   */
  @GET("/rest/{apiKey}/info.json/{zip}/degrees")
  suspend fun getLocationInfo(
    @Path("apiKey") apiKey: String,
    @Path("zip") zipCode: String
  ): Response<Location>
}