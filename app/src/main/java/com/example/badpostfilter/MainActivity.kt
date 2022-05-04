package com.example.badpostfilter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.badpostfilter.database.AppDatabase
import com.example.badpostfilter.database.Thought
import com.example.badpostfilter.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter
    private val thoughts = mutableListOf<Thought>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.thoughtsList.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(
            applicationContext, layoutManager.orientation
        )
        binding.thoughtsList.addItemDecoration(dividerItemDecoration)

        adapter = MainAdapter()
        binding.thoughtsList.adapter = adapter

        loadThoughts()
    }

    private fun loadThoughts() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val dao = db.thoughtDao()
            val results = dao.getAllThoughts()

            withContext(Dispatchers.Main) {
                thoughts.clear()
                thoughts.addAll(results)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result : ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {
            loadThoughts()
        }
    }

    private fun addNewThought() {
        val intent = Intent(applicationContext, AddThoughtActivity::class.java)
        startForResult.launch(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_thought_menu_item) {
            addNewThought()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    inner class MainViewHolder(val view: TextView) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        init {
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }

        override fun onLongClick(p0: View?): Boolean {
            TODO("Not yet implemented")
        }
    }

    inner class MainAdapter() : RecyclerView.Adapter<MainViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.thought_item_view, parent, false) as TextView

            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.view.text = thoughts[position].toString()
        }

        override fun getItemCount(): Int {
            return thoughts.size
        }
    }
}