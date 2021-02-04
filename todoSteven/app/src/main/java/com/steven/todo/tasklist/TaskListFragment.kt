package com.steven.todo.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.steven.todo.R
import com.steven.todo.network.Api
import com.steven.todo.task.TaskActivity
import kotlinx.coroutines.launch


class TaskListFragment : Fragment() {
    companion object {
        const val ADD_TASK_REQUEST_CODE = 666

        const val KEY_EDIT = "reply_key_edit"

    }


    private val viewModel: TasksViewModel by viewModels()
    private val adapter = TaskListAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rooterView = inflater.inflate(R.layout.fragment_task_list, container, false)

        viewModel.tasklist.observe(viewLifecycleOwner) { newList ->
            adapter.submitList(newList)
        }

        return rooterView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Pour une [RecyclerView] ayant l'id "recycler_view":
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter


        adapter.onDeleteTask = { task ->
            lifecycleScope.launch {
                viewModel.deleteTask(task)

            }
            viewModel.tasklist.observe(viewLifecycleOwner) { list ->
                adapter.submitList(list)
            }
        }

        val openTaskActivity = view.findViewById<FloatingActionButton>(R.id.add_task)

        openTaskActivity.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        adapter.onEditTask = { task ->
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
                lifecycleScope.launch {
                    viewModel.updateTask(task)
                    viewModel.tasklist.observe(viewLifecycleOwner) { list ->
                        adapter.submitList(list)
                    }
                }

            } else {
                lifecycleScope.launch {
                    viewModel.createTask(task)
                    viewModel.tasklist.observe(viewLifecycleOwner) { list ->
                        adapter.submitList(list)

                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val user = Api.userService.getInfo().body()!!
            val textUser = view?.findViewById<TextView>(R.id.user)
            textUser?.text = "${user.firstName} ${user.lastName}"
            //val tasks = Api.taskWebService.getTasks().body()!!
        }

        lifecycleScope.launch {
            viewModel.refresh()
        }
    }

}