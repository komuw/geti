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

    override fun run(arg: String): String {
        println("ExampleTask.run called with arg: $arg ")
        return arg
    }
}


fun main(): Unit {
    val tsk = ExampleTask()
    println("main")
    println(tsk.taskName)

    // assuming the run method has a signature like;
    // .run( name)
    tsk.delay("John")

    tsk.delay("kili")
    tsk.delay("mili")


    val worker = Worker(task = tsk, workerId = "workerId")
    worker.consumeTasks()
}
