package s.yarlykov.fixdataproto.domain

import io.reactivex.Observable

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