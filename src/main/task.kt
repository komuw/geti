package geti

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class TaskArgs(val arg: String)


interface BaseTask {
    val broker: BaseBroker
    val queueName: String
    val taskName: String
    /**
    [drainDuration] is the duration(in seconds) that a worker should wait
    after getting a termination signal(SIGTERM, SIGQUIT etc).
    during this duration, the worker does not consumer anymore tasks from the broker,
    the worker will continue executing any tasks that it had already dequeued from the [broker]
     */
    val drainDuration: Float
    val logLevel: String
    val json: Json

    fun run(arg: String): Any

    fun delay(arg: String): Unit {
        println("Task.delay called with arg: $arg ")

        broker.check(queueName)
        val jsonData = json.stringify(TaskArgs.serializer(), TaskArgs(arg = arg))
        broker.enqueue(queueName = queueName, item = jsonData)
    }
}
