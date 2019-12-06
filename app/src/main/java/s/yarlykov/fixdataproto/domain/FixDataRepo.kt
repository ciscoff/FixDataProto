package s.yarlykov.fixdataproto.domain

import io.reactivex.Observable

interface FixDataRepo {
    fun connect() : Observable<FixData>
}