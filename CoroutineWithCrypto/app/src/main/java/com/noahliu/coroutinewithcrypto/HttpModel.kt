package com.noahliu.coroutinewithcrypto

import android.os.SystemClock
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import okhttp3.*
import java.io.IOException

object HttpModel {

    fun sendGet(url:String):String{
        Log.d("TAG", "sendGet $url ")
        val arrayList = ArrayList<String>()
        val client = OkHttpClient().newBuilder().build()
        val request = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                arrayList.add("UnknownHostException")
            }

            override fun onResponse(call: Call, response: Response) {
                arrayList.add(response.body!!.string())
            }
        })//respond
        while (arrayList.isEmpty()) {
            SystemClock.sleep(1)
            if (arrayList.isNotEmpty()) break
        }
        return try {
            arrayList[0]
        } catch (e: Exception) {
            e.toString()
        }
    }

    suspend fun getAllCoin(array: Array<String>): List<String> {
        return coroutineScope {
            return@coroutineScope (array.indices).map {
                async(Dispatchers.IO) {
                    return@async sendGet(
                        "https://api.coingecko.com/api/v3/coins/${array[it]}"
                    )
                }
            }.awaitAll()
        }
    }

}