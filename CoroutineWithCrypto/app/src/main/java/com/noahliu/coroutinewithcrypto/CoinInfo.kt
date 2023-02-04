package com.noahliu.coroutinewithcrypto

class CoinInfo {
    var id: String = ""
    var name:String = ""
    var image:Image? = null
    var market_data:MarketData? = null


    class MarketData{
        var current_price:CurrentPrice? = null
    }
    class CurrentPrice{
        var usd:Double = 0.0
        var twd:Double = 0.0
        var jpy:Double = 0.0
    }

    class Image{
        var thumb:String = ""
        var small:String = ""
        var large:String = ""
    }

}