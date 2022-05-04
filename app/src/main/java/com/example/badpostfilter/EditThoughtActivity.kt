package com.example.badpostfilter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.badpostfilter.database.AppDatabase
import com.example.badpostfilter.database.Thought
import com.example.badpostfilter.databinding.ActivityEditThoughtBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditThoughtActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityEditThoughtBinding
    private var thoughtId : Long = -1
    private var thoughtStatus : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditThoughtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Edit Thought"
        thoughtId = intent.getLongExtra(getString(R.string.intent_key_thought_id), -1)
        CoroutineScope(Dispatchers.IO).launch {
            val thought = AppDatabase.getDatabase(applicationContext).thoughtDao().getThought(thoughtId)

            withContext(Dispatchers.Main) {
                binding.fieldTitle.setText(thought.title)
                binding.fieldThought.setText(thought.thought)
                thoughtStatus = thought.approved
            }
        }
        binding.btnUpdate.setOnClickListener(this)
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
            val record = Thought(thoughtId, title, thought, thoughtStatus)
            thoughtDao.updateThought(record)

            val intent = Intent()

            withContext(Dispatchers.Main) {
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}