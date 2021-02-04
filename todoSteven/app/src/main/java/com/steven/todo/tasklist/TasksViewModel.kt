package com.steven.todo.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TasksViewModel : ViewModel() {

    private val repository = TasksRepository()

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()

    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val tasklist: LiveData<List<Task>> = _taskList

    fun refresh() {
        viewModelScope.launch {
            _taskList.value = repository.refresh()
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch {
            val newTask = repository.createTask(task)
            val table = _taskList.value.orEmpty().toMutableList()
            if (newTask != null) {
                table.add(newTask)
            }
            _taskList.value = table
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            val deleteTask = repository.deleteTask(task)

            val table = _taskList.value.orEmpty().toMutableList()
            if (deleteTask != null) {
                table.remove(deleteTask)
            }
            _taskList.value = table
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch{
            val updateTask = repository.updateTask(task)

            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { updateTask?.id == it.id }
            editableList[position] = task
            _taskList.value = editableList
        }
    }
}