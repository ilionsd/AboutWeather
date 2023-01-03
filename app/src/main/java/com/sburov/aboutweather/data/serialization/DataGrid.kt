package com.sburov.aboutweather.data.serialization

open class DataGrid<K>(
    val data: Map<K, Array<Any?>>,
) {
    val size: Int
    init {
        val it = data.values.iterator()
        size = it.next().size
        while (it.hasNext()) {
            assert(size == it.next().size)
        }
    }


    inline operator fun <V> get(row: K): Array<V?>? = data[row] as? Array<V?>?
    inline operator fun <V> get(row: K, col: Int): V? = data[row] ?. get(col) as? V?
}

public inline operator fun <T> DataGrid<T>.iterator(): Iterator<Map.Entry<T, Array<Any?>>> = data.entries.iterator()
