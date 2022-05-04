package com.example.badpostfilter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.badpostfilter.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter
    private val thoughts = mutableListOf<String>() // change to Thought class once implemented

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
            // replace this line with call to DB
            val results = mutableListOf<String>("this", "is", "dummy", "data")

            withContext(Dispatchers.Main) {
                thoughts.clear()
                thoughts.addAll(results)
                adapter.notifyDataSetChanged()
            }
        }
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