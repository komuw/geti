package geti

import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.json.Json


@Serializable
data class TaskArgs<T>(val args: List<T>)


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

    fun run(vararg args: String): Any {
        println("Task.run called with args: $args :: argsAsList: ${args.toList()}")
        return 1
    }

    fun delay(vararg args: String): Unit {
        println("Task.delay called with args: $args :: argsAsList: ${args.toList()}")

        broker.check(queueName)
        val jsonData = json.stringify(TaskArgs.serializer(StringSerializer), TaskArgs(args = args.toList()))
        broker.enqueue(queueName = queueName, item = jsonData)
    }
}

