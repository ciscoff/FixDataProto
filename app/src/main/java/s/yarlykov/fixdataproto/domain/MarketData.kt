package s.yarlykov.fixdataproto.domain

data class MarketData(val value : Int, val time : Long = System.currentTimeMillis())