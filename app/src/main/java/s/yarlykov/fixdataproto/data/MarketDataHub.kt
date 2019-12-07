package s.yarlykov.fixdataproto.data

import io.reactivex.Observable
import s.yarlykov.fixdataproto.domain.Granularity
import s.yarlykov.fixdataproto.domain.MarketData
import s.yarlykov.fixdataproto.domain.MarketDataProvider

class MarketDataHub(providers: List<MarketDataProvider>) {

    private val dataProviders = HashMap<String, MarketDataProvider>()

    init {
        providers.forEach { p ->
            dataProviders[p.getName()] = p
        }
    }

    fun marketDataStream(
        name: String,
        granularity: Granularity = Granularity.ONE
    ): Observable<List<MarketData>> {
        return dataProviders[name]!!.getDataStream(granularity)
    }
}