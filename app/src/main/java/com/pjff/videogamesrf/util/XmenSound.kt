package com.pjff.videogamesrf.util

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.pjff.videogamesrf.R


class XmenSound : Service() {

    private lateinit var player: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        player = MediaPlayer.create(this, R.raw.xmen)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player.start()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        player.stop()
        player.release()
        stopSelf()

        super.onDestroy()
    }


}