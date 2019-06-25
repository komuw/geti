import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Data(val a: Int, val b: String = "42")


open class BaseBroker {
    open fun check(queueName: String): Unit {}

    open fun enqueue(queueName: String, item: String): Unit {}

    open fun dequeue(queueName: String): String {
        return "TODO: implement BaseBroker.dequeue"
    }

    open fun done(queueName: String, item: String, state: String): Unit {} // "task.TaskState"

    open fun shutdown(queueName: String, duration: Float): Unit {}
}

class InMemoryBroker : BaseBroker() {
    /*
     This broker should only be used for:
      (i) tests
      (ii) demos
      (iii) the watchdog task.
    Do not use this broker in production or anywhere else that you care about.
     */
    override fun check(queueName: String) {
        println("InMemoryBroker.check called with     queueName: $    queueName")
    }

    override fun enqueue(queueName: String, item: String): Unit {
        println("InMemoryBroker.enqueue called with     queueName: $    queueName and item: $item")
    }

    override fun dequeue(queueName: String): String {
        println("InMemoryBroker.dequeue called with     queueName: $    queueName ")
        return "TODO: implement InMemoryBroker.dequeue "
    }

    override fun done(
            queueName: String,
            item: String,
            state: String
    ): Unit { // replace type of state with "task.TaskState"
        println("InMemoryBroker.done called with     queueName: $    queueName item: $item and state: $state ")
    }

    override fun shutdown(queueName: String, duration: Float): Unit {
        println("InMemoryBroker.shutdown called with     queueName: $    queueName duration: $duration")
    }
}

class Task(
        val broker: BaseBroker,
        val queueName: String,
        val taskName: String,
        /**
        [drainDuration] is the duration(in seconds) that a worker should wait
        after getting a termination signal(SIGTERM, SIGQUIT etc).
        during this duration, the worker does not consumer anymore tasks from the broker,
        the worker will continue executing any tasks that it had already dequeued from the [broker]
         */
        private val drainDuration: Float = 10.0f,
        private val logLevel: String = "INFO"
) {

    fun aha(): Unit {
        println(broker)
        println(queueName)
        println(taskName)
        println(drainDuration)
        println(logLevel)
    }

    fun delay(vararg args: Any): Unit {
        println("Task.delay called with args: $args and argsAsList: ${args.toList()}")
        val numberOfArgs = args.size
        println("numberOfArgs: $numberOfArgs")
        for (item in args) {
            println(item)
        }
        // TODO: json seriliaze the args and then enqueue them in the broker
        broker.enqueue(queueName = queueName, item = "DummyItem")
    }
}


fun main(): Unit {
    val b = InMemoryBroker()
    val tsk = Task(broker = b, queueName = "queueName", taskName = "MyTaskName")
    println("main")
    println(tsk.aha())
    println(tsk.taskName)

    // assuming the run method has a signature like;
    // .run(log_id, age, name)
    tsk.delay("qejq4j242", 90, "John")



    val json = Json(JsonConfiguration.Stable)
    // serializing objects
    val jsonData = json.stringify(Data.serializer(), Data(42))

    println()
    println("jsonData")
    println(jsonData)
    println()
}