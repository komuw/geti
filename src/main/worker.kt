package geti


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

        val obj: TaskArgs = task.json.parse(TaskArgs.serializer(), item)
        println()
        println("parsed json obj")
        println(obj)
        println()

        task.run(obj.arg)
    }
}
