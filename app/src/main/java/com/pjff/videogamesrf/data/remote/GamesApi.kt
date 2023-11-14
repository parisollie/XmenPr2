package com.pjff.videogamesrf.data.remote

import com.pjff.videogamesrf.data.remote.model.GameDetailDto
import com.pjff.videogamesrf.data.remote.model.GameDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface GamesApi {

    @GET
    fun getGames(
        @Url url: String?
    ): Call<List<GameDto>>

    @GET("cm/games/game_detail.php")
    fun getGameDetail(
        @Query("id") id: String?
    ): Call<GameDetailDto>


    @GET("xmen/xmen_list")
    fun getGamesApiary(): Call<List<GameDto>>

    //games/game_detail/21357
    @GET("xmen/xmen_detail/{id}")
    fun getGameDetailApiary(
        @Path("id") id: String?/*,
        @Path("name") name: String?*/
    ): Call<GameDetailDto>

}