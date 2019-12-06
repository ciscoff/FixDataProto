package s.yarlykov.fixdataproto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import s.yarlykov.fixdataproto.application.FixApp
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        disposable.add(
            (application as FixApp)
                .getHub()
                .getDataStream()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val message = "${it.hashCode().toString(16)}"
                    tvFixData.text = message
                }
        )
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }
}


