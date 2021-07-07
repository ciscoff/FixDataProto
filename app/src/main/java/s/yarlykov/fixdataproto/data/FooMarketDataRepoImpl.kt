package s.yarlykov.fixdataproto.data

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import s.yarlykov.fixdataproto.domain.MarketData
import s.yarlykov.fixdataproto.domain.MarketDataRepo
import s.yarlykov.fixdataproto.logIt
import java.util.concurrent.TimeUnit
import kotlin.random.Random

const val FOO_PRICE_MIN = 1
const val FOO_PRICE_MAX = 20

class FooMarketDataRepoImpl : MarketDataRepo() {

    override fun connect(): Observable<MarketData> =
        Observable
            .interval(1, TimeUnit.SECONDS, Schedulers.newThread())
            .map {
                MarketData(Random.nextInt(FOO_PRICE_MIN, FOO_PRICE_MAX),
                    timeLineHandler.getMarker(System.currentTimeMillis()))
            }
            .doOnNext {
                logIt("${it.value} in ${it.marker.time}")
            }
            .publish()
            .refCount()
}