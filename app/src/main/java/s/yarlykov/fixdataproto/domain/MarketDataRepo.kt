package s.yarlykov.fixdataproto.domain

import io.reactivex.Observable

interface MarketDataRepo {
    fun connect() : Observable<MarketData>
}