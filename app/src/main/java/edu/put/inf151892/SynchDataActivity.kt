package edu.put.inf151892

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SynchDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_synch_data)
        val cache = getSharedPreferences("cache", MODE_PRIVATE)
    }
}