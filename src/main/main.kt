package geti

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class HttpTask : BaseTask {
    override val broker = InMemoryBroker()
    override val queueName = "queueName"
    override val taskName = "MyTaskName"
    override val drainDuration: Float = 10.0f
    override val logLevel: String = "INFO"
    override val json: Json = Json(JsonConfiguration.Stable)

    override fun run(args: HashMap<String, String>): Unit {
        println("ExampleTask.run called with args: $args ")
    }
}


fun main(): Unit {
    val tsk = HttpTask()
    println("main")
    println(tsk.taskName)

    // assuming the run method has a signature like;
    // .run( name)
    tsk.delay(hashMapOf("name" to "komu", "age" to "27"))

    tsk.delay(hashMapOf("name" to "Jean", "age" to "37"))
    tsk.delay(hashMapOf("name" to "Ole", "age" to "57"))


    val worker = Worker(task = tsk, workerId = "workerId")
    worker.consumeTasks()
}
