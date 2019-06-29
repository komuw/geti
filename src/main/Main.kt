package geti

import com.github.kittinunf.fuel.Fuel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class HttpTask : BaseTask {
    /**
     * This task makes http requests and
     * then prints the response
     */
    override val broker = InMemoryBroker()
    override val queueName = "queueName"
    override val taskName = "MyTaskName"
    override val drainDuration: Float = 10.0f
    override val logLevel: String = "INFO"
    override val json: Json = Json(JsonConfiguration.Stable)

    override suspend fun run(args: HashMap<String, String>) {
        println("ExampleTask.run called with args: $args ")

        val req = Fuel.get(args["url"] as String)
        val resp = req.responseString()
        println(resp)
    }
}

fun main() {
    val tsk = HttpTask()
    println("main")
    println(tsk.taskName)

    // queue tasks
    tsk.schedule(hashMapOf("url" to "https://httpbin.org/get"))
    tsk.schedule(hashMapOf("url" to "https://httpbin.org/schedule/3"))
    tsk.schedule(hashMapOf("url" to "https://httpbin.org/schedule/7"))

    for (i in 1..30) {
        tsk.schedule(hashMapOf("url" to "https://httpbin.org/schedule/2"))
    }

    // run workers
    val worker = Worker(task = tsk, workerId = "workerId")
    worker.consumeTasks()
}
