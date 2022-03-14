package com.example.individualproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.forEach
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
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-task")
            .allowMainThreadQueries()
            .build()
        val taskDao = db.taskDao()
        val tasks = taskDao.getAll()
        tasks.forEach {
            if (it.isDone) {
                doneListData.add(it)
            } else {
                taskData.add(it)
            }
        }

        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.list_view)
        doneView = findViewById(R.id.list_view_done)
        addButton = findViewById(R.id.addButton)
        clearButton = findViewById(R.id.clearButton)
        inputText = findViewById(R.id.inputText)
        val doneText = findViewById<TextView>(R.id.doneTextView)
        val todoText = findViewById<TextView>(R.id.todotextView)
        doneText.visibility = if (doneListData.isEmpty()) View.INVISIBLE else View.VISIBLE
        todoText.visibility = if (taskData.isEmpty()) View.INVISIBLE else View.VISIBLE
        val adapter: ArrayAdapter<Task> = ArrayAdapter(this, android.R.layout.simple_list_item_checked, taskData)
        val doneAdapter: ArrayAdapter<Task> = ArrayAdapter(this, android.R.layout.simple_list_item_checked, doneListData)
        listView.adapter = adapter
        doneView.adapter = doneAdapter
        fun setIsDoneCheckMarks () {
            for (i in 0..doneAdapter.count) {
                doneView.setItemChecked(i, true)
            }
        }
        setIsDoneCheckMarks()


        addButton.setOnClickListener {
            val text = inputText.text.toString()
            if (text.isBlank()) {
                val toast = Toast.makeText(this, "Enter task.", Toast.LENGTH_SHORT)
                toast.show()
            } else {
                val task = Task(inputText.text.toString(), false)
                taskData.add(task)
                db.taskDao().insertAll(task)
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
            db.taskDao().deleteTodos()
            adapter.notifyDataSetChanged()
        }
        doneText.setOnClickListener {
            doneListData.clear()
            doneText.visibility = View.INVISIBLE
            db.taskDao().deleteDone()
            doneAdapter.notifyDataSetChanged()
        }
        listView.setOnItemClickListener { adapterView, view, pos, l ->
            doneText.visibility = View.VISIBLE
            taskData[pos].isDone = true
            db.taskDao().updateTasks(taskData[pos])
            doneListData.add(taskData[pos])
            taskData.removeAt(pos)
            if (taskData.isEmpty()) {
                todoText.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()
            doneAdapter.notifyDataSetChanged()
            setIsDoneCheckMarks()

        }

        doneView.setOnItemClickListener { adapterView, view, pos, l ->
            todoText.visibility = View.VISIBLE
            doneListData[pos].isDone = false
            db.taskDao().updateTasks(doneListData[pos])
            taskData.add(doneListData[pos])
            doneListData.removeAt(pos)
            doneView.setItemChecked(pos, true)
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


}