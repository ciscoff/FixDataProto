package s.yarlykov.fixdataproto.domain.time

enum class TimeEvent(val value : Int) {
    SECOND(1),
    MINUTE(60),
    HOUR(3600),
    DAY(24 * 60 * 60)
}