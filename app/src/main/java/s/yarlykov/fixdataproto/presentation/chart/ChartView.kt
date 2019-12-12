package s.yarlykov.fixdataproto.presentation.chart

import s.yarlykov.fixdataproto.domain.ChartOptions
import s.yarlykov.fixdataproto.domain.MarketData

interface ChartView {
    fun setChartOptions(options : ChartOptions)
    fun update(data : List<MarketData>)
}