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

    override fun run(args: HashMap<String, String>) {
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
    tsk.delay(hashMapOf("url" to "https://httpbin.org/get"))
    tsk.delay(hashMapOf("url" to "https://httpbin.org/delay/3"))
    tsk.delay(hashMapOf("url" to "https://httpbin.org/delay/7"))

    // run workers
    val worker = Worker(task = tsk, workerId = "workerId")
    worker.consumeTasks()
}
