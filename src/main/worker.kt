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
    }
}
