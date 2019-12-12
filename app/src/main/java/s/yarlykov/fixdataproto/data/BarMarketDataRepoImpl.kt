package s.yarlykov.fixdataproto.data

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import s.yarlykov.fixdataproto.domain.MarketData
import s.yarlykov.fixdataproto.domain.MarketDataRepo
import java.util.concurrent.TimeUnit
import kotlin.random.Random

const val BAR_PRICE_MIN = 50
const val BAR_PRICE_MAX = 70

class BarMarketDataRepoImpl : MarketDataRepo() {

    override fun connect(): Observable<MarketData> =
        Observable
            .interval(1, TimeUnit.SECONDS, Schedulers.newThread())
            .map {
                MarketData(
                    Random.nextInt(BAR_PRICE_MIN, BAR_PRICE_MAX),
                    timeLineHandler.getMarker(System.currentTimeMillis())
                )
            }
            .publish()
            .refCount()
}