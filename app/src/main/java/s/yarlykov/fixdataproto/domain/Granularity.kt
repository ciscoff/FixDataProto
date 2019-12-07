package s.yarlykov.fixdataproto.domain

enum class Granularity(val seconds : Int) {
    ONE(1),
    FIVE(5),
    TEN(10),
    HALF(30)
}