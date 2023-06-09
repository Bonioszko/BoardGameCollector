package edu.put.inf151892


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    private lateinit var gamesButton: Button
    private lateinit var dlcButton: Button
    private lateinit var synchButton: Button
    private lateinit var clearDataButton: Button
    private lateinit var userName: TextView
    private lateinit var dlcCount: TextView
    private lateinit var GamesCount:TextView
    private lateinit var synchDate:TextView

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
        db.deleteAllExtensions()
        db.deleteAllImages()
        cache.edit().putBoolean("confDone",false).apply()
        cache.edit().putString("synchDate", "")
        revokeUriPermission(null, 0)
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
            val db = DBHandler(this)
            val dlcNumber =db.numberOfDlc()
            val gameNumber = db.numberOfGames()
            val username = cache.getString("username","")
            val synchdate = cache.getString("syncDate","")
            if (synchdate != null) {
                Log.d("date", synchdate)
            }
            userName = findViewById(R.id.userName)
            userName.text = "Username: " + username
            synchDate = findViewById(R.id.dateSynch)
            synchDate.append(synchdate)
            GamesCount = findViewById(R.id.gameCounter)
            GamesCount.append(gameNumber.toString())
            dlcCount = findViewById(R.id.dlcCounter)
            dlcCount.append(dlcNumber.toString())
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

            clearDataButton = findViewById(R.id.clearDataButton)
            clearDataButton.setOnClickListener(){
            clearData()
            }
        }


    }
}