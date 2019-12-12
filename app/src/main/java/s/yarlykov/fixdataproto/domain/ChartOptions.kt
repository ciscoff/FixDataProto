package s.yarlykov.fixdataproto.domain

data class ChartOptions(
    val title : String,
    val axisX : String,
    val axisY: String,
    val min : Int,
    val max : Int)