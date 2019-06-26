package geti

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class ExampleTask : BaseTask {
    override val broker = InMemoryBroker()
    override val queueName = "queueName"
    override val taskName = "MyTaskName"
    override val drainDuration: Float = 10.0f
    override val logLevel: String = "INFO"
    override val json: Json = Json(JsonConfiguration.Stable)

    override fun run(vararg args: String): Any {
        println("ExampleTask.run called with args: $args :: argsAsList: ${args.toList()}")
        return args
    }
}


fun main(): Unit {
    val tsk = ExampleTask()
    println("main")
    println(tsk.taskName)

    // assuming the run method has a signature like;
    // .run(log_id, age, name)
    tsk.delay("qejq4j242", "90", "John")

    tsk.delay("222", "22", "kili")
    tsk.delay("333", "333", "mili")


    val worker = Worker(task = tsk, workerId = "workerId")
    worker.consumeTasks()
}