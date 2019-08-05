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
            val tArgs: TaskArgs = task.json.parse(TaskArgs.serializer(), item)

            launch(Dispatchers.IO) {
                // TODO: add  CoroutineName("geti-worker-coroutine")
                //the dispatcher use; is backed by a shared pool of threads.
                // Additional threads in this pool are created and are shutdown on demand.
                // The number of threads defaults to 64 threads or the number of cores (whichever is larger).
                task.run(tArgs.args)
            }

        }
    }
}
