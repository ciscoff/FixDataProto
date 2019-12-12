package s.yarlykov.fixdataproto.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import s.yarlykov.fixdataproto.R
import s.yarlykov.fixdataproto.application.TradingApp
import s.yarlykov.fixdataproto.data.FOO_PRICE_MAX
import s.yarlykov.fixdataproto.data.FOO_PRICE_MIN
import s.yarlykov.fixdataproto.domain.ChartOptions
import s.yarlykov.fixdataproto.domain.MarketData
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

        val hub = (application as TradingApp).getHub()

        graph.setChartOptions(ChartOptions(
            getString(R.string.foo),
            "x",
            "y",
            FOO_PRICE_MIN,
            FOO_PRICE_MAX
        ))

        disposable.add(
            hub
                .marketDataStream(getString(R.string.foo))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val message = it.print()
                    tvFoo.text = message
                    graph.update(it)
                }
        )

        disposable.add(
            hub
                .marketDataStream(getString(R.string.bar))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val message = it.print()
                    tvBar.text = message
                }
        )
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }

    private fun List<MarketData>.print() : String {

        val sdf = SimpleDateFormat("ss", Locale.getDefault())

        val li = mutableListOf<String>()
        this.forEach {md ->
            if(md.value > 0) {
                val s = "${"%02d".format(md.value)}: ${sdf.format(md.marker.time)}s"
                li.add(s)
            }
        }
        return li.joinToString()
    }
}


