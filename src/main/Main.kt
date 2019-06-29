package geti


import com.github.kittinunf.fuel.Fuel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class HttpTask : BaseTask {
    /**
     * This task makes http requests and
     * then prints the response
     */
    override val broker = InMemoryBroker()
    override val queueName = "HttpTask"
    override val taskName = "HttpTask"
    override val drainDuration: Float = 10.0f
    override val logLevel: String = "INFO"
    override val json: Json = Json(JsonConfiguration.Stable)

    override suspend fun run(args: HashMap<String, String>) {
        println("HttpTask.run called with args: $args ")

        val req = Fuel.get(args["url"] as String)
        val resp = req.responseString()
        println(resp)
    }
}


class PrintTask : BaseTask {
    override val broker = InMemoryBroker()
    override val queueName = "PrintTask"
    override val taskName = "PrintTask"
    override val drainDuration: Float = 10.0f
    override val logLevel: String = "INFO"
    override val json: Json = Json(JsonConfiguration.Stable)

    override suspend fun run(args: HashMap<String, String>) {
        println("PrintTask.run called with args: $args ")
        delay(6000L)
    }
}


fun main() = runBlocking<Unit> {
    val tsk = HttpTask()
    println("main")
    println(tsk.taskName)

    // queue tasks
    tsk.schedule(hashMapOf("url" to "https://httpbin.org/get"))
    tsk.schedule(hashMapOf("url" to "https://httpbin.org/delay/3"))
    tsk.schedule(hashMapOf("url" to "https://httpbin.org/delay/7"))

    for (i in 1..8) {
        tsk.schedule(hashMapOf("url" to "https://httpbin.org/delay/2"))
    }

    val pt = PrintTask()
    for (i in 1..30) {
        pt.schedule(hashMapOf("name" to "John"))
    }

    // run workers
    launch(Dispatchers.Default) {
        val worker1 = Worker(task = tsk, workerId = "workerId-1")
        worker1.consumeTasks()
    }

    launch(Dispatchers.Default) {
        val worker2 = Worker(task = pt, workerId = "workerId-2")
        worker2.consumeTasks()
    }
}
