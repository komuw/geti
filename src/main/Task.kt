package geti

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class TaskArgs(val args: HashMap<String, String>)

interface BaseTask {
    val broker: BaseBroker
    val queueName: String
    val taskName: String
    /**
     * [drainDuration] is the duration(in seconds) that a worker should wait
     * after getting a termination signal(SIGTERM, SIGQUIT etc).
     * during this duration, the worker does not consumer anymore tasks from the broker,
     * the worker will continue executing any tasks that it had already dequeued from the [broker]
     */
    val drainDuration: Float
    val logLevel: String
    val json: Json

    suspend fun run(args: HashMap<String, String>): Any

    fun schedule(args: HashMap<String, String>) {
        println("Task.schedule called with args: $args ")

        broker.check(queueName)
        val jsonData = json.stringify(TaskArgs.serializer(), TaskArgs(args = args))
        broker.enqueue(queueName = queueName, item = jsonData)
    }
}
