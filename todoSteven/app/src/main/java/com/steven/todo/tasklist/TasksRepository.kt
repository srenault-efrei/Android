package com.steven.todo.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.steven.todo.network.Api

class TasksRepository {

    private val tasksWebService = Api.taskWebService

    suspend fun refresh(): List<Task>? {
        // Call HTTP (opération longue):
        val response = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun createTask(task: Task): Task? {
        val response = tasksWebService.createTask(task)
        return if (response.isSuccessful) response.body() as Task else null
    }

    suspend fun deleteTask(task: Task): Task? {
        val response = tasksWebService.deleteTask(task.id)
        return if (response.code() == 204) task else null
    }

    suspend fun updateTask(task: Task): Task? {
        val response = tasksWebService.updateTask(task, task.id)
        return if (response.code() == 200) task else null
    }
}