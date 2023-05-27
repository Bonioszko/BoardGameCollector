package edu.put.inf151892


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var gamesButton: Button
    private lateinit var dlcButton: Button
    private lateinit var synchButton: Button
    private lateinit var clearDataButton: Button
    private lateinit var configButton: Button


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
    fun clearData(){
        val cache = getSharedPreferences("cache", Context.MODE_PRIVATE)
        val db = DBHandler(this)
        db.deleteAllBoardGames()
        cache.edit().putBoolean("confDone",false).apply()
        finishAffinity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cache = getSharedPreferences("cache", Context.MODE_PRIVATE)
        Log.d("tag",cache.getBoolean("confDone",true).toString())
        if (!cache.getBoolean("confDone", true)){
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
        }
        else{
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
            configButton = findViewById(R.id.conifgButton)
            configButton.setOnClickListener(){
                val intent = Intent(this, ConfigActivity::class.java)
                startActivity(intent)
            }
            clearDataButton = findViewById(R.id.clearDataButton)
            clearDataButton.setOnClickListener(){
            clearData()
            }
        }


    }
}