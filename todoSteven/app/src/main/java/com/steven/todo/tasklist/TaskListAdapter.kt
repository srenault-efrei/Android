package com.steven.todo.tasklist

import android.icu.text.CaseMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.steven.todo.R

 class TaskListAdapter(private val tasklist: List<String>) :
    RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {


    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(taskTitle: String) {
            itemView.apply {
                val textViewTask = itemView.findViewById<TextView>(R.id.task_title)
                textViewTask.text = taskTitle
            }
        }
    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
         val itemView  = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return  TaskViewHolder(itemView)

     }

     override fun getItemCount(): Int {
        return tasklist.size
     }

     override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
      holder.bind(tasklist[position])
     }
 }