package com.steven.todo.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.steven.todo.R
import com.steven.todo.tasklist.Task
import com.steven.todo.tasklist.TaskListFragment
import java.util.*


class TaskActivity : AppCompatActivity() {


    companion object {
        const val KEY = "reply_key"
        const val OLD_KEY = "reply_old_key"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task2)
        val confirmButton = findViewById<Button>(R.id.add_task)
        val task = intent.getSerializableExtra(TaskListFragment.KEY_EDIT)
        val title = findViewById<EditText>(R.id.editText_add_title)
        val description = findViewById<EditText>(R.id.editText_add_description)


        if (task != null) {
            task as Task
            title.setText(task.title)
            description.setText((task.description))
        }

        confirmButton.setOnClickListener {
            if (task == null) {
                val newTask = Task(id = UUID.randomUUID().toString(), title = title.text.toString(), description = description.text.toString())
                intent.putExtra(KEY, newTask)

            } else  {
                task as Task
                val newTask = Task(id = task.id, title = title.text.toString(), description = description.text.toString())
                intent.putExtra(KEY, newTask)
                intent.putExtra(OLD_KEY,task)

            }

            setResult(RESULT_OK, intent)
            finish()

        }

    }


}
