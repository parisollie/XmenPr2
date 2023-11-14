package com.pjff.videogamesrf.data.remote.model

import com.google.gson.annotations.SerializedName

//---------------- PR2 -------------------

data class GameDto(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("thumbnail")
    var thumbnail: String? = null,
    @SerializedName("name")
    var name: String? = null
)


