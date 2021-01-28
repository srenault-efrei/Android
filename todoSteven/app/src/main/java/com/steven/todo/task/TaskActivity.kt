package com.steven.todo.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.steven.todo.R
import com.steven.todo.tasklist.Task
import java.util.*


class TaskActivity : AppCompatActivity() {


    companion object {
        const val KEY = "reply_key"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task2)
        val fab =  findViewById<Button>(R.id.add_task)
        fab.setOnClickListener{
            val title = findViewById<EditText>(R.id.editText_add_title)
            val description = findViewById<EditText>(R.id.editText_add_description)
            val newTask = Task(id = UUID.randomUUID().toString(), title = title.text.toString()  , description = description.text.toString())
            intent.putExtra(KEY, newTask)
            setResult(RESULT_OK, intent)
            finish()
        }

    }


}
