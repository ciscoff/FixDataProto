package s.yarlykov.fixdataproto.application

import android.app.Application
import s.yarlykov.fixdataproto.R
import s.yarlykov.fixdataproto.data.BarMarketDataRepoImpl
import s.yarlykov.fixdataproto.data.FooMarketDataRepoImpl
import s.yarlykov.fixdataproto.data.MarketDataHub
import s.yarlykov.fixdataproto.domain.MarketDataProvider
import s.yarlykov.fixdataproto.domain.MarketDataProviderImpl

class TradingApp : Application() {

    private lateinit var marketDataHub: MarketDataHub

    override fun onCreate() {
        super.onCreate()

        val list = listOf<MarketDataProvider>(
            MarketDataProviderImpl(getString(R.string.foo), FooMarketDataRepoImpl()),
            MarketDataProviderImpl(getString(R.string.bar), BarMarketDataRepoImpl())
        )

        marketDataHub = MarketDataHub(list)
    }

    fun getHub() = marketDataHub
}