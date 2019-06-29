package geti

import kotlinx.coroutines.Dispatchers
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

            launch(Dispatchers.Default) {
                // TODO: add  CoroutineName("geti-worker-coroutine")
                //the dispatcher use; is backed by a shared pool of threads on JVM.
                // the max no of threads used is equal to the number of CPU cores.
                task.run(obj.args)
            }

        }
    }
}
