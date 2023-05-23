package edu.put.inf151892

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var gamesButton: Button
    private lateinit var dlcButton: Button
    private lateinit var synchButton: Button
    private lateinit var clearDataButton: Button

    fun moveToGames(){
        val intent = Intent(this, ListGamesActivity::class.java)
        startActivity(intent)

    }
    fun moveToDLC(){
        val intent = Intent(this, DlcActivity::class.java)
        startActivity(intent)
    }
    fun moveToSynch(){
        val intent = Intent(this, SynchDataActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gamesButton = findViewById(R.id.gamesButton)
        gamesButton.setOnClickListener {
            moveToGames()
        }
        dlcButton = findViewById(R.id.dlcButton)
        dlcButton.setOnClickListener{
            moveToDLC()
        }
        synchButton = findViewById(R.id.synchButton)
        synchButton.setOnClickListener{
            moveToSynch()
        }
    }
}