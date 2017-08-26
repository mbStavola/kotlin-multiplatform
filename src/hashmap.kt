class HashMap<KEY : Any, VALUE>(
        initialCapacity: Int = 32,
        private val RESIZE_FACTOR: Double = 2.0,
        private val THRESHOLD_FACTOR: Double = 1.5
) {
    var size = 0
        private set
    val capacity
        get() = content.size
    var threshold = (initialCapacity * THRESHOLD_FACTOR).toInt()
        private set

    private var content: Array<EntryLinkedList<KEY, VALUE>?> = Array(initialCapacity) { null }
    private var usedIndicies: Array<Int?> = Array(initialCapacity) { null }

    fun put(key: KEY, value: VALUE) {
        val index = key.mask()

        val node = entryNodeOf(key, value)
        content[index]?.insertHead(node)

        size++
        // Mark the used index

        if (size >= threshold) {
            val targetSize = (size * RESIZE_FACTOR).toInt()
            resize(targetSize)
        }
    }

    fun get(key: KEY): VALUE? {
        val index = key.mask()

        return content[index]?.get(key) { current, key ->
            current?.key != key
        }?.data?.value
    }

    fun remove(key: KEY): Boolean {
        val index = key.mask()

        val deleted = content[index]?.remove(key) { current, key ->
            current?.key == key
        } ?: false

        if (deleted) {
            size--
        }

        // If we free a bucket, we need to unmark the used index

        return deleted
    }

    @Suppress("UNCHECKED_CAST")
    fun entries(): Array<Entry<KEY, VALUE>> {
        val entries: Array<Entry<KEY, VALUE>?> = Array(size) { null }

        forEach { i, (value) ->
            entries[i] = value
        }

        return entries as Array<Entry<KEY, VALUE>>
    }

    @Suppress("UNCHECKED_CAST")
    fun keys(): Array<KEY> {
        val entries: Array<Any?> = Array(size) { null }

        forEach { i, (key) ->
            entries[i] = key
        }

        return entries as Array<KEY>
    }

    @Suppress("UNCHECKED_CAST")
    fun values(): Array<VALUE> {
        val entries: Array<Any?> = Array(size) { null }

        forEach { i, (_, value) ->
            entries[i] = value
        }

        return entries as Array<VALUE>
    }

    private inline fun forEach(block: (Int, EntryNode<KEY, VALUE>) -> Unit) {

    }

    // We need a better way to do this... also without hashcode()
    private fun KEY.mask() = hashCode() % capacity

    private fun resize(targetSize: Int) {
        threshold = (targetSize * THRESHOLD_FACTOR).toInt()

        val newContent: Array<EntryLinkedList<KEY, VALUE>?> = Array(targetSize) { null }
        val newUsedIndicies: Array<Int?> = Array(targetSize) { null }

        forEach { _, node ->
            val index = node.data.key.mask()
            newContent[index]?.insertHead(node)
            // Mark new used indicies
        }

        content = newContent
        usedIndicies = newUsedIndicies
    }
}

data class Entry<out KEY, out VALUE>(val key: KEY, val value: VALUE)

internal typealias EntryLinkedList<KEY, VALUE> = LinkedList<Entry<KEY, VALUE>>
internal typealias EntryNode<KEY, VALUE> = Node<Entry<KEY, VALUE>>
internal fun <KEY, VALUE> entryNodeOf(key: KEY, value: VALUE) = Node(
        data = Entry(key, value),
        next = null
)