package geti

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class ExampleTask(
        override val broker: BaseBroker,
        override val queueName: String,
        override val taskName: String,
        override val drainDuration: Float = 10.0f,
        override val logLevel: String = "INFO",
        override val json: Json = Json(JsonConfiguration.Stable)
) : BaseTask {
    override fun run(vararg args: String): Any {
        println("ExampleTask.run called with args: $args :: argsAsList: ${args.toList()}")
        return args
    }
}


fun main(): Unit {
    val b = InMemoryBroker()
    val tsk = ExampleTask(broker = b, queueName = "queueName", taskName = "MyTaskName")
    println("main")
    println(tsk.taskName)

    // assuming the run method has a signature like;
    // .run(log_id, age, name)
    tsk.delay("qejq4j242", "90", "John")

    tsk.delay("222", "22", "kili")
    tsk.delay("333", "333", "mili")
}