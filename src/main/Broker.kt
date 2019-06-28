package geti

interface BaseBroker {
    fun check(queueName: String): Unit
    fun enqueue(queueName: String, item: String): Unit

    fun dequeue(queueName: String): String

    fun done(queueName: String, item: String, state: String): Unit // TODO: change sttate type to be "task.TaskState"

    fun shutdown(queueName: String, duration: Float): Unit
}

class InMemoryBroker : BaseBroker {
    /*
     This broker should only be used for:
      (i) tests
      (ii) demos
      (iii) the watchdog task.
    Do not use this broker in production or anywhere else that you care about.

    {
        "queue1": ["item1", "item2", "item3"],
        "queue2": ["item1", "item2", "item3"]
        ...
    }
     */
    private val store = HashMap<String, ArrayList<String>>()

    override fun check(queueName: String) {
        println("InMemoryBroker.check called with queueName: $queueName")
        if (!store.containsKey(queueName)) {
            store[queueName] = ArrayList<String>()
        }
    }

    override fun enqueue(queueName: String, item: String) {
        println("InMemoryBroker.enqueue called with queueName: $queueName and item: $item")
        if (store.containsKey(queueName)) {
            store[queueName]?.add(item)
        } else {
            store[queueName] = ArrayList<String>()
            store[queueName]?.add(item)
        }
        println("store")
        println(store)
    }

    override fun dequeue(queueName: String): String {
        println("InMemoryBroker.dequeue called with queueName: $queueName ")
        val item: String? = store[queueName]?.removeAt(0)

        if (item.isNullOrEmpty()) {
            println("the queue is empty. maybe sleep")
            // TODO: sleep in a coroutine
        }
        return item as String
    }

    override fun done(
            queueName: String,
            item: String,
            state: String
    ) { // replace type of state with "task.TaskState"
        println("InMemoryBroker.done called with queueName: $queueName item: $item and state: $state ")
    }

    override fun shutdown(queueName: String, duration: Float) {
        println("InMemoryBroker.shutdown called with queueName: $queueName duration: $duration")
    }
}