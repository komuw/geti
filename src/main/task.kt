import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

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


@Serializable
data class TaskArgs<T>(val args: List<T>)

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

    private val json = Json(JsonConfiguration.Stable)

    fun aha(): Unit {
        println(broker)
        println(queueName)
        println(taskName)
        println(drainDuration)
        println(logLevel)
    }

    fun delay(vararg args: String): Unit {
        println("Task.delay called with args: $args and argsAsList: ${args.toList()}")
        val jsonData = json.stringify(TaskArgs.serializer(StringSerializer), TaskArgs(args = args.toList()))
        broker.enqueue(queueName = queueName, item = jsonData)
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
    tsk.delay("qejq4j242", "90", "John")
}