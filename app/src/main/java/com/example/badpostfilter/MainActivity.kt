package com.example.badpostfilter

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.badpostfilter.database.AppDatabase
import com.example.badpostfilter.database.Thought
import com.example.badpostfilter.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

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

        val swipeToDeleteCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val thought = thoughts[viewHolder.adapterPosition]
                thoughts.removeAt(viewHolder.adapterPosition)
                adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                CoroutineScope(Dispatchers.IO).launch {
                    AppDatabase.getDatabase(applicationContext).thoughtDao().deleteThought(thought)
                }
                Toast.makeText(applicationContext, "Thought deleted successfully", Toast.LENGTH_LONG).show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                c.clipRect(0f, viewHolder.itemView.top.toFloat(), dX, viewHolder.itemView.bottom.toFloat())
                c.drawColor(Color.RED)
                val textMargin = 42
                val trashBinIcon = resources.getDrawable(R.drawable.ic_baseline_delete_42, null)
                trashBinIcon.bounds = Rect(
                    textMargin,
                    viewHolder.itemView.top + textMargin,
                    textMargin + trashBinIcon.intrinsicWidth,
                    viewHolder.itemView.top + trashBinIcon.intrinsicHeight + textMargin
                )
                trashBinIcon.draw(c)

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        val deleteHelper = ItemTouchHelper(swipeToDeleteCallback)
        deleteHelper.attachToRecyclerView(binding.thoughtsList)
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