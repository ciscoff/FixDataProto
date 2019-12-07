package s.yarlykov.fixdataproto.domain

import io.reactivex.Observable

interface MarketDataProvider {
    fun getName() : String
    fun getDataStream(granularity: Granularity) : Observable<List<MarketData>>
}