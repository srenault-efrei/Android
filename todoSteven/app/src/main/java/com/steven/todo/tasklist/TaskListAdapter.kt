package com.steven.todo.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.steven.todo.R


object TaskDiffCallback : DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
       return  oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
      return  oldItem.title == oldItem.title && oldItem.description == newItem.description
    }
}

class TaskListAdapter: androidx.recyclerview.widget.ListAdapter<Task ,TaskListAdapter.TaskViewHolder> (TaskDiffCallback) {

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

    var onDeleteTask: ((Task) -> Unit)? = null

    var onEditTask: ((Task) -> Unit)? = null

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        holder.bind(getItem(position), getItem(position).title + '\n' + getItem(position).description)
    }


}