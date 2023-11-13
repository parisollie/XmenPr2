package com.pjff.videogamesrf.data.remote.model

import com.google.gson.annotations.SerializedName

data class GameDetailDto(

//---------------------------------PR2------------------------------

    @SerializedName("name")
    var name: String? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("long_desc")
    var longDesc: String? = null,
    @SerializedName("Level")
    var levelX: String? = null,
    @SerializedName("First Time")
    var first_Time: String? = null,
    @SerializedName("Power")
    var power: String? = null,
    @SerializedName("vid_desc")
    var vidDesc: String? = null,
    @SerializedName("locLat")
    var log_Lat: Double? = null,
    @SerializedName("locLong")
    var log_Long: Double? = null
)

