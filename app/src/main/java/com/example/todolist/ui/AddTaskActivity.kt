package com.example.todolist.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityAddTaskBinding
import com.example.todolist.datasource.TaskDataSource
import com.example.todolist.extensions.format
import com.example.todolist.extensions.text
import com.example.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

/*
A Acitivity sintética está decreapted,
agora usa-se o binding, evitando erros
de null nos componentes da tela e
conseguindo evitar os findViewById.
*/

class AddTaskActivity : AppCompatActivity(){
    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.textInputEditText.text = it.title
                binding.tilDate.text = it.date
                binding.tilTime.text = it.hour
            }
        }

        insertListeners()
    }

    private fun insertListeners() {
        //Campo de data
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            datePicker.addOnPositiveButtonClickListener {
                val offset = TimeZone.getDefault().getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_TAG" )
        }
        //Campo de hora
        binding.tilTime.editText?.setOnClickListener{
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                //Minuto
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                //Hora
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                //Format pra mostrar
                binding.tilTime.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, null )
        }
        //Campo de cancelar
        binding.btnCancelTask.setOnClickListener {
            finish()
        }
        //Botão de criar task
        binding.btnNewTask.setOnClickListener {
            val task = Task(
                title = binding.textInputEditText.text,
                date = binding.tilDate.text,
                hour = binding.tilTime.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}