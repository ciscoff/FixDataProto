package s.yarlykov.fixdataproto.domain

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

/**
 * Класс реализует двусвязный список на массиве. Массив удобен для первичной инииализации.
 * Двусвязный список нужен потому, что котировки кладем в одном направлении, а отрисовываем
 * в противоположном (от свежих к старым)
 */

class MarketDataProviderImpl(
    private val name: String,
    repo: MarketDataRepo,
    capacity: Int = 10
) : MarketDataProvider {

    // Массив для хранения котировок
    private var history: Array<Link> = Array(capacity) { index ->
        Link(index, MarketData(0), null, null)
    }

    // Этот указатель будет передвигаться по кругу и указывать
    // на следующий "свободный" контейнер
    private var head: Link = history[0]

    private val aggregatedDataStream = BehaviorSubject.create<List<MarketData>>()

    private val rawDataObserver = object : Observer<MarketData> {
        override fun onComplete() {
        }

        override fun onSubscribe(d: Disposable) {
        }

        override fun onNext(fixData: MarketData) {
            head.marketData = fixData
            head = head.next!!
            aggregatedDataStream.onNext(collectDescend())
        }

        override fun onError(e: Throwable) {
        }
    }

    init {

        // Закольцевать связи
        // a. Хвост ссылается на голову и наоборот
        history[history.lastIndex].next = history[0]
        history[0].prev = history[history.lastIndex]

        // b. Закольцевать все оставшиеся элементы
        history.withIndex().forEach { next ->

            if (next.index < history.lastIndex) {
                next.value.next = history[next.index + 1]
            }
            if (next.index > 0) {
                history[next.index].prev = history[next.index - 1]
            }
        }

        // Подписка на котировки
        repo.connect().subscribe(rawDataObserver)
    }

    override fun getName() = name

    override fun getDataStream(granularity: Granularity): Observable<List<MarketData>> =
        when (granularity) {
            Granularity.ONE,
            Granularity.FIVE,
            Granularity.TEN,
            Granularity.HALF -> aggregatedDataStream.hide()
        }

    // Список котировок по убывающей дате
    private fun collectDescend(): List<MarketData> {
        val list = mutableListOf<MarketData>()

        var item = head
        val id = head.id

        do {
            list.add(item.prev!!.marketData.copy())
            item = item.prev!!
        } while (item.id != id)

        return list
    }

    // "Звено" двусвязного списка
    data class Link(val id: Int, var marketData: MarketData, var next: Link?, var prev: Link?) {
        override fun toString() = "id=$id, coupled=${next != null && prev != null}"
    }
}