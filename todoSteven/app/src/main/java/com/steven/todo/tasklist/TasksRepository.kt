package com.steven.todo.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.steven.todo.network.Api

class TasksRepository {
    private val tasksWebService = Api.taskWebService

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()

    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks!!
        }
    }

    suspend fun createTask(task: Task) {
        val tasksResponse = tasksWebService.createTask(task)
        if (tasksResponse.isSuccessful) {
            val task = tasksResponse.body() as Task
            val addTable = _taskList.value.orEmpty().toMutableList()
            addTable.add(task)
            System.out.println("create")
            _taskList.value = addTable

        }
    }

    suspend fun deleteTask(task: Task) {
        val tasksResponse = tasksWebService.deleteTask(task.id)
        if (tasksResponse.isSuccessful) {
            val deleteTable = _taskList.value.orEmpty().toMutableList()
            deleteTable.remove(task)
            System.out.println("delete")
            _taskList.value = deleteTable
        }
    }

}