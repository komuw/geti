package geti

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Worker(private val task: BaseTask, private val workerId: String) {
    /**
    docs
     */

    fun consumeTasks() = runBlocking<Unit> {
        println("workerId: $workerId")
        while (true) {
            val item: String = task.broker.dequeue(task.queueName)
            println("dequeue item")
            println(item)

            val obj: TaskArgs = task.json.parse(TaskArgs.serializer(), item)
            println()
            println("parsed json obj")
            println(obj)
            println()

            launch {
                task.run(obj.args)
            }
        }
    }
}
