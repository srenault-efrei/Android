package com.steven.todo.tasklist

import android.R.attr.data
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.steven.todo.R
import java.util.*


class TaskListFragment : Fragment() {
    private val taskList = mutableListOf<Task>(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2", description = "description 2"),
        Task(id = "id_3", title = "Task 3", description = "description 3")

    )
   // private val adapter = (TaskListAdapter(taskList))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rooterView = inflater.inflate(R.layout.fragment_task_list, container, false)

        return rooterView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Pour une [RecyclerView] ayant l'id "recycler_view":
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = (TaskListAdapter(taskList))

// Instanciation d'un objet task avec des données préremplies:
        val newTask: Task =
            Task(
                id = UUID.randomUUID().toString(),
                title = "Task ${taskList.size + 1}",
                description = "description ${taskList.size + 1}"
            )
        val btCreate = view.findViewById<FloatingActionButton>(R.id.add_task)
        btCreate.setOnClickListener {

            taskList.add(newTask)
            recyclerView.adapter?.notifyItemChanged(taskList.count() - 1)
        }

        (recyclerView.adapter as TaskListAdapter).onDeleteTask = { task ->
            taskList.remove(task)
            recyclerView.adapter?.notifyItemChanged(taskList.count())

        }

    }
}