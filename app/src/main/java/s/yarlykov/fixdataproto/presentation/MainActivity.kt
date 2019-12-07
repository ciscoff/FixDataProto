package s.yarlykov.fixdataproto.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import s.yarlykov.fixdataproto.R
import s.yarlykov.fixdataproto.application.TradingApp
import s.yarlykov.fixdataproto.domain.MarketData

class MainActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val hub = (application as TradingApp).getHub()

        disposable.add(
            hub
                .marketDataStream(getString(R.string.foo))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val message = it.print()
                    tvFoo.text = message
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

    fun List<MarketData>.print() : String {

        val li = mutableListOf<String>()

        this.forEach {md ->
            li.add(md.value.toString())
        }

        return li.joinToString()
    }
}


