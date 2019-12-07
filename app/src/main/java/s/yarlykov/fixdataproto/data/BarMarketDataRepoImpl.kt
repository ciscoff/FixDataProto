package s.yarlykov.fixdataproto.data

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import s.yarlykov.fixdataproto.domain.MarketData
import s.yarlykov.fixdataproto.domain.MarketDataRepo
import s.yarlykov.fixdataproto.logIt
import java.util.concurrent.TimeUnit
import kotlin.random.Random

private const val PRICE_MIN = 50
private const val PRICE_MAX = 70

class BarMarketDataRepoImpl : MarketDataRepo {

    override fun connect(): Observable<MarketData> =
        Observable
            .interval(1, TimeUnit.SECONDS, Schedulers.newThread())
            .map {
                MarketData(Random.nextInt(PRICE_MIN, PRICE_MAX))
            }
            .doOnNext {
                logIt("${it.value} in ${it.time}")
            }
            .publish()
            .refCount()
}