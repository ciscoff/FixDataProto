package s.yarlykov.fixdataproto.domain

import s.yarlykov.fixdataproto.domain.time.TimeLineMarker

data class MarketData(
    val value: Int,
    val marker: TimeLineMarker
)