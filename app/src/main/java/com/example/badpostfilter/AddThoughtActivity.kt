package com.example.badpostfilter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.badpostfilter.database.AppDatabase
import com.example.badpostfilter.database.Thought
import com.example.badpostfilter.databinding.ActivityAddThoughtBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddThoughtActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityAddThoughtBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddThoughtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Add Note"
        binding.btnCreate.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val title = binding.fieldTitle.getText().toString().trim()
        if (title.isEmpty()) {
            Toast
                .makeText(applicationContext, "Title cannot be empty", Toast.LENGTH_LONG)
                .show()
            return
        }

        val thought = binding.fieldThought.getText().toString().trim()
        if (thought.isEmpty()) {
            Toast
                .makeText(applicationContext, "Thought cannot be empty", Toast.LENGTH_LONG)
                .show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val thoughtDao = AppDatabase.getDatabase(applicationContext).thoughtDao()
            val record = Thought(null, title, thought, false)
            thoughtDao.addThought(record)

            val intent = Intent()

            withContext(Dispatchers.Main) {
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}