package s.yarlykov.fixdataproto.data

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import s.yarlykov.fixdataproto.domain.FixData
import s.yarlykov.fixdataproto.domain.FixDataRepo
import s.yarlykov.fixdataproto.logIt
import java.util.concurrent.TimeUnit
import kotlin.random.Random

private const val PRICE_MIN = 1
private const val PRICE_MAX = 20

class FixDataRepoImpl : FixDataRepo {

    override fun connect(): Observable<FixData> =
        Observable
            .interval(1, TimeUnit.SECONDS, Schedulers.newThread())
            .map {
                FixData(Random.nextInt(PRICE_MIN, PRICE_MAX))
            }
            .doOnNext {
                logIt("${it.value} in ${it.time}")
            }
            .publish()
            .refCount()
}