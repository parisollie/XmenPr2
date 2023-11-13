package com.pjff.videogamesrf.data.remote.model

import com.google.gson.annotations.SerializedName

//Para poder consumir nuestra respuesta
/*
Para esto es cuando tenemos la lista

"id": "5036954",
 "thumbnail": "https://www.serverbpw.com/cm/games/imgs/zeldasst.png",
 "title": "Zelda: Skyward Sword"
*/
/*data class GameDto(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("thumbnail")
    var thumbnail: String? = null,
    @SerializedName("title")
    var title: String? = null
)*/

//---------------- PR2 -------------------

data class GameDto(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("thumbnail")
    var thumbnail: String? = null,
    @SerializedName("name")
    var name: String? = null
)

/*

 "id": "21357",

    "thumbnail": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT_UcND_8CGXUE0skiE2V73HhmaXOdKw-us3A&usqp=CAU",

    "name": "Jean Gray"
*/
