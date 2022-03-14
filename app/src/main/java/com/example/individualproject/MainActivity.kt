package com.example.individualproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.android.material.textfield.TextInputEditText
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var doneView: ListView
    private lateinit var addButton: Button
    private lateinit var clearButton: Button
    private lateinit var inputText:  TextInputEditText
    private var taskData = ArrayList<Task>()
    private var doneListData = ArrayList<Task>()
    lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "MYDB")
            .allowMainThreadQueries()
            .build()
        val taskDao = db.taskDao()
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.list_view)
        doneView = findViewById(R.id.list_view_done)
        addButton = findViewById(R.id.addButton)
        clearButton = findViewById(R.id.clearButton)
        inputText = findViewById(R.id.inputText)
        val doneText = findViewById<TextView>(R.id.doneTextView)
        val todoText = findViewById<TextView>(R.id.todotextView)
        doneText.visibility = View.INVISIBLE
        todoText.visibility = View.INVISIBLE
        val adapter: ArrayAdapter<Task> = ArrayAdapter(this, android.R.layout.simple_list_item_checked, taskData)
        val doneAdapter: ArrayAdapter<Task> = ArrayAdapter(this, android.R.layout.simple_list_item_checked, doneListData)
        listView.adapter = adapter
        doneView.adapter = doneAdapter

        addButton.setOnClickListener {
            val text = inputText.text.toString()
            if (text.isBlank()) {
                val toast = Toast.makeText(this, "Enter task.", Toast.LENGTH_SHORT)
                toast.show()
            } else {
                val task = Task(inputText.text.toString(), false)
                taskData.add(task)
                todoText.visibility = View.VISIBLE
                inputText.setText("")
//                tasks.add(Task(inputText.text.toString(), false))
                adapter.notifyDataSetChanged()
            }

        }
        clearButton.setOnClickListener {
            inputText.setText("")
        }
        todoText.setOnClickListener {
            taskData.clear()
            todoText.visibility = View.INVISIBLE
            adapter.notifyDataSetChanged()
        }
        doneText.setOnClickListener {
            doneListData.clear()
            doneText.visibility = View.INVISIBLE
            doneAdapter.notifyDataSetChanged()
        }
        listView.setOnItemClickListener { adapterView, view, pos, l ->
            doneText.visibility = View.VISIBLE
            doneListData.add(taskData[pos])
            taskData.removeAt(pos)
            if (taskData.isEmpty()) {
                todoText.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()
            doneAdapter.notifyDataSetChanged()
            for (i in 0..doneAdapter.count) {
                doneView.setItemChecked(i, true)
        }

        }

        doneView.setOnItemClickListener { adapterView, view, pos, l ->
            todoText.visibility = View.VISIBLE
            taskData.add(doneListData[pos])
            doneListData.removeAt(pos)
            if (doneListData.isEmpty()) {
                doneText.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()
            doneAdapter.notifyDataSetChanged()

            for (i in 0..adapter.count) {
                listView.setItemChecked(i, false)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        taskData.forEach {
            db.taskDao().insertAll(it)
        }
    }



}