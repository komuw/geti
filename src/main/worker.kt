package geti

import kotlinx.serialization.internal.StringSerializer

class Worker(private val task: BaseTask, private val workerId: String) {
    /**
    docs
     */

    fun consumeTasks() {
        println("workerId: $workerId")
//        while (true) {
//            val item: String = task.broker.dequeue(task.queueName)
//            println("dequeue item")
//            println(item)
//        }

        val item: String = task.broker.dequeue(task.queueName)
        println("dequeue item")
        println(item)

        val obj: TaskArgs<String> = task.json.parse(TaskArgs.serializer(StringSerializer), item)
        println()
        println("parsed json obj")
        println(obj)
        println()

        task.run(*obj.args.toTypedArray())
    }
}
