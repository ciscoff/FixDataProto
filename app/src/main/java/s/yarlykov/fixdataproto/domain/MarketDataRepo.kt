package s.yarlykov.fixdataproto.domain

import io.reactivex.Observable
import s.yarlykov.fixdataproto.domain.time.TimeLineHandler

abstract class MarketDataRepo {

    val timeLineHandler = TimeLineHandler()

    abstract fun connect(): Observable<MarketData>
}