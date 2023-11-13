package com.pjff.videogamesrf.application

import android.app.Application
import com.pjff.videogamesrf.data.GameRepository
import com.pjff.videogamesrf.data.remote.RetrofitHelper

//Instancia global de la aplicacion

class VideoGamesRFApp: Application() {

    private val retrofit by lazy{
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy{
        GameRepository(retrofit)
    }

}