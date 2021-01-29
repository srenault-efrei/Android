package com.steven.todo.tasklist

import android.R.attr.data
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.steven.todo.R
import com.steven.todo.task.TaskActivity
import java.util.*


class TaskListFragment : Fragment() {
    companion object {
        const val ADD_TASK_REQUEST_CODE = 666

        const val KEY_EDIT = "reply_key_edit"

    }


    private val taskList = mutableListOf<Task>(
            Task(id = "id_1", title = "Task 1", description = "description 1"),
            Task(id = "id_2", title = "Task 2", description = "description 2"),
            Task(id = "id_3", title = "Task 3", description = "description 3")

    )
    private val adapter = TaskListAdapter(taskList)

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

        (recyclerView.adapter as TaskListAdapter).onDeleteTask = { task ->
            taskList.remove(task)
            recyclerView.adapter?.notifyDataSetChanged()
        }

        val openTaskActivity = view.findViewById<FloatingActionButton>(R.id.add_task)

        openTaskActivity.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        (recyclerView.adapter as TaskListAdapter).onEditTask = { task ->
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(KEY_EDIT, task)

            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = data!!.getSerializableExtra(TaskActivity.KEY) as Task

            if (data!!.getSerializableExtra(TaskActivity.OLD_KEY) != null) {

                val editTask = data!!.getSerializableExtra(TaskActivity.OLD_KEY) as Task
                val indexEditTask = taskList.indexOf(editTask)

                System.out.println(taskList.indexOf(editTask))
                System.out.println(taskList[indexEditTask])

                val removeTask = taskList[indexEditTask]
                taskList.remove(removeTask)
                // taskList.add(task)
                 adapter.notifyDataSetChanged()
            }
            taskList.add(task)
            adapter.notifyDataSetChanged()
        }
    }
}