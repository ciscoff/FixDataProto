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

class FixDataHub(rawDataStream: Observable<FixData>, capacity: Int = 100) {

    // Массив для хранения котировок
    private var storage: Array<Link> = Array(capacity) { index ->
        Link(index, FixData(0), null, null)
    }

    // Этот указатель будет просто передвигаться по кругу
    private var head: Link = storage[0]

    private val formatedDataStream = BehaviorSubject.create<List<FixData>>()

    private val dataObserver = object : Observer<FixData> {
        override fun onComplete() {
        }

        override fun onSubscribe(d: Disposable) {
        }

        override fun onNext(fixData: FixData) {
            head.fixData = fixData
            head = head.next!!
            formatedDataStream.onNext(collect())
        }

        override fun onError(e: Throwable) {
        }
    }

    init {

        // Закольцевать связи
        // a. Хвост ссылается на голову и наоборот
        storage[storage.lastIndex].next = storage[0]
        storage[0].prev = storage[storage.lastIndex]

        // b. Закольцевать все оставшиеся элементы
        storage.withIndex().forEach { next ->

            if (next.index < storage.lastIndex) {
                next.value.next = storage[next.index + 1]
            }
            if (next.index > 0) {
                storage[next.index].prev = storage[next.index - 1]
            }
        }

        // Подписка на котировки
        rawDataStream
            .subscribe(dataObserver)
    }

    fun getDataStream() = formatedDataStream.hide()



    private fun collect(): List<FixData> {
        val list = mutableListOf<FixData>()

        var item = head
        val id = head.id

        do {
            list.add(item.prev!!.fixData.copy())
            item = item.prev!!
        } while (item.id != id)

        return list
    }

    data class Link(val id: Int, var fixData: FixData, var next: Link?, var prev: Link?) {
        override fun toString() = "id=$id, coupled=${next != null && prev != null}"
    }
}