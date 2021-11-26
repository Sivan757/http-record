package cn.kapukapu.plugins

import java.util.concurrent.ConcurrentLinkedQueue

/**
 *
 *
 * @author Sivan
 */
object HistoryInMemoryCache {
    const val size = 50
    private val cache = ConcurrentLinkedQueue<String>()

    fun put(content: String) {
        if (cache.size <= 50) {
            cache.add(content)
        } else {
            cache.poll()
            cache.add(content)
        }
    }

    fun get(): Sequence<Pair<Int, String>> {
        return cache.reversed()
            .asSequence()
            .mapIndexed { index, s -> Pair(index, s) }
    }
}