package s.yarlykov.fixdataproto.application

import android.app.Application
import s.yarlykov.fixdataproto.data.FixDataRepoImpl
import s.yarlykov.fixdataproto.domain.FixDataHub
import s.yarlykov.fixdataproto.domain.FixDataRepo

class FixApp : Application() {
    private lateinit var fixDataRepo : FixDataRepo
    private lateinit var fixDataHub: FixDataHub

    override fun onCreate() {
        super.onCreate()
        fixDataRepo = FixDataRepoImpl()
        fixDataHub = FixDataHub(fixDataRepo.connect())
    }

    fun getStream() = fixDataRepo
    fun getHub() = fixDataHub
}