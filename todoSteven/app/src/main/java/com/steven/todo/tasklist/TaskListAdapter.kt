package com.steven.todo.tasklist

import android.content.Intent
import android.icu.text.CaseMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.steven.todo.R
import com.steven.todo.task.TaskActivity

class TaskListAdapter(private val tasklist: MutableList<Task>) :
        RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task, taskTitle: String) {
            itemView.apply {
                val textViewTask = itemView.findViewById<TextView>(R.id.task_title)
                textViewTask.text = taskTitle

                val btDelete = itemView.findViewById<ImageButton>(R.id.delete_task)
                btDelete.setOnClickListener {
                    onDeleteTask?.invoke(task)

                }
                val btEdit = itemView.findViewById<ImageButton>(R.id.edit_task)
                btEdit.setOnClickListener {
                    onEditTask?.invoke(task)


                }

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return tasklist.size
    }

    var onDeleteTask: ((Task) -> Unit)? = null

    var onEditTask: ((Task) -> Unit)? = null

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasklist[position], tasklist[position].title + '\n' + tasklist[position].description)
    }


}