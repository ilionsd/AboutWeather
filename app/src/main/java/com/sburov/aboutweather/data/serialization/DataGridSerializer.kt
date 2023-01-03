package com.sburov.aboutweather.data.serialization

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeCollection

open class DataGrid<T>(
    val data: Map<T, Array<Any?>>,
) {
    val size: Int
    init {
        val it = data.values.iterator()
        size = it.next().size
        while (it.hasNext()) {
            assert(size == it.next().size)
        }
    }

    inline fun <reified V> getDataAs(key: T): Array<V?> = get(key) as Array<V?>

    fun get(key: T): Array<Any?>? = data[key]
}

public inline operator fun <T> DataGrid<T>.iterator(): Iterator<Map.Entry<T, Array<Any?>>> = data.entries.iterator()


open class DataGridSerializer<T>(
    serialDescriptor: SerialDescriptor,
    private val nameSerializer: KSerializer<T>,
    private val dataTypeSerializers: Map<T, KSerializer<Any?>>
): KSerializer<DataGrid<T>> {
    override val descriptor = serialDescriptor

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: DataGrid<T>) = encoder.encodeCollection(descriptor, value.size){
        var index = 0
        for ((name, data) in value) {
            val dataSerializer = ArraySerializer(dataTypeSerializers[name]!!)
            encodeSerializableElement(descriptor, index++, nameSerializer, name)
            encodeSerializableElement(descriptor, index++, dataSerializer, data)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): DataGrid<T> = decoder.decodeStructure(descriptor) {
        val dataGrid: MutableMap<T, Array<Any?>> = mutableMapOf()
        if (decodeSequentially()) {
            val size = decodeCollectionSize(descriptor)
            var index = 0
            while (index < size) {
                val name = decodeSerializableElement(descriptor, index++, nameSerializer)
                val dataSerializer = ArraySerializer(dataTypeSerializers[name]!!)
                val data = decodeSerializableElement(descriptor, index++, dataSerializer)
                dataGrid[name] = data
            }
        }
        else {
            while (true) {
                val keyIndex = decodeElementIndex(descriptor)
                if (DECODE_DONE == keyIndex) {
                    break
                }
                val name: T = decodeSerializableElement(descriptor, keyIndex, nameSerializer)
                val valIndex = decodeElementIndex(descriptor)
                if (valIndex != keyIndex + 1) {
                    throw IllegalArgumentException("Value must follow key in a map, index for key: $keyIndex, returned index for value: $valIndex")
                }
                val data: Array<Any?> = decodeSerializableElement(descriptor, valIndex, ArraySerializer(dataTypeSerializers[name]!!))
                dataGrid[name] = data;
            }
        }
        return@decodeStructure DataGrid(dataGrid)
    }
}
