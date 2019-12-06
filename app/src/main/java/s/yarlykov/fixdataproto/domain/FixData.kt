package s.yarlykov.fixdataproto.domain

data class FixData(val value : Int, val time : Long = System.currentTimeMillis())