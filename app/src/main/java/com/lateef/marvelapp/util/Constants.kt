package com.lateef.marvelapp.util

import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

class Constants {

    companion object{
        const val BASE_URL = "https://gateway.marvel.com/"
        val timeStamp = Timestamp(System.currentTimeMillis()).time.toString()
        //const val API_KEY = "f8a47a3d3684c33b639a3187e3c5615e"
        const val PRIVATE_KEY = "11eb2830218b105e3769fec3737f2c4dae88db71"
        const val API_KEY = "f8a47a3d3684c33b639a3187e3c5615e"
        //const val PRIVATE_KEY = "f8a47a3d3684c33b639a3187e3c5615e"
        const val limit = "20"
        fun hash(): String{
            val input = "$timeStamp$PRIVATE_KEY$API_KEY"
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1,md.digest(input.toByteArray())).toString(16).padStart(32,'0')
        }
    }
}