package com.example.cleanarchitecture.presentation

import android.content.Context
import android.media.MediaPlayer
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.example.cleanarchitecture.R


class CustomToasts {

    companion object{

        fun toastSuccess(context: Context){
            val song = MediaPlayer.create(context, R.raw.succes)
            song.start()
            val toast = Toast(context)
            val view = LayoutInflater.from(context).inflate(
                R.layout.toast_success,
                null,
                false
            )
            toast.setView(view)
            toast.duration = Toast.LENGTH_SHORT
            toast.setGravity(Gravity.CENTER_VERTICAL,0,0)
            toast.show()
            song.start()
        }

    }

}