package com.noahliu.coroutinewithcrypto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var adapter:RecyclerViewAdapter
    private val array = arrayOf("bitcoin","ethereum","dogecoin","evmos","likecoin","bitsong","tezos")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView:RecyclerView = findViewById(R.id.recyclerview_display)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter(this)
        recyclerView.adapter = adapter


        GlobalScope.launch {
            val getCoin = runBlocking {
                return@runBlocking HttpModel.getAllCoin(array)
            }
            val list = ArrayList<CoinInfo>()
            val gson = Gson()
            getCoin.forEach {
                val info = gson.fromJson(it,CoinInfo::class.java)
                if (info.name != ""){
                    list.add(info)
                }
            }
            runOnUiThread {
                adapter.setData(list)
                Toast.makeText(this@MainActivity,"資料已完成",Toast.LENGTH_LONG).show()
            }
        }
    }
}