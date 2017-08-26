class LinkedList<DATA>(node: Node<DATA>) {
    var head: Node<DATA>? = node
        private set
    var size = 1
        private set

    fun insertHead(other: Node<DATA>) {
        other.next = head
        head = other
        size++
    }

    fun insertTail(other: Node<DATA>) {
        var current: Node<DATA>? = head
        while (current?.next != null) {
            current = current.next
        }

        current?.next = other
        size++
    }

    fun get(data: DATA): Node<DATA>? {
        return get(data) { current, test ->
            current != test
        }
    }

    inline fun <TEST> get(data: TEST, compare: (DATA?, TEST) -> Boolean): Node<DATA>? {
        var current: Node<DATA>? = head
        while (compare(current?.data, data)) {
            current = current?.next
        }

        return current
    }

    fun remove(data: DATA): Boolean {
        return remove(data) { current, test ->
            current == test
        }
    }

    // We can't make this inline because we access private set methods :(
    fun <TEST> remove(data: TEST, compare: (DATA?, TEST) -> Boolean): Boolean {
        var deleted = false

        var previous: Node<DATA>? = null
        var current: Node<DATA>? = head

        do {
            val match = compare(current?.data, data)
            if (previous == null && match) {
                // We need to make the next node the root node... but how?
                head = current?.next
                size--
                deleted = true
            } else if (previous != null && match) {
                previous.next = current?.next
                size--
                deleted = true
            }

            previous = current
            current = current?.next
        } while (current?.next != null)

        return deleted
    }
}

data class Node<DATA>(val data: DATA, var next: Node<DATA>?)