package com.example.todolist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.datasource.TaskDataSource
import com.example.todolist.ui.AddTaskActivity
 import com.example.todolist.ui.TaskListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvTasks.adapter = adapter

        updateList()
        insertListener()
    }

    private fun insertListener() {
        binding.btnAddTask.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }
        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }
        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) updateList()
    }

    private fun updateList() {
        val list = TaskDataSource.getList()
        //Evita repetir códigos atribuido o if
        binding.includeEmpty.emptyState.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        adapter.submitList(list)
    }
    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}