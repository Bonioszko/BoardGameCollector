package edu.put.inf151892

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import models.Boardgame
import models.XmlParserTask

class SynchDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_synch_data)
        val cache = getSharedPreferences("cache", MODE_PRIVATE)
        val progressBar: ProgressBar = findViewById(R.id.progressBar) // Znajdź ProgressBar na podstawie ID
        progressBar.progress = 0
        progressBar.max = 100
        val syncButton :Button = findViewById(R.id.sync)
        syncButton.setOnClickListener(){
            val db = DBHandler(this)

            db.deleteAllExtensions()
            db.deleteAllBoardGames()
            var url  = "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString(
                "username",
                ""
            ) +
                    "&subtype=boardgame&excludesubtype=boardgameexpansion"
            val boardgamesList =  XmlParserTask().execute(url)
            progressBar.setProgress(25,true)
            var urlExtensions  = "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString(
                "username",
                ""
            ) +
                    "&subtype=boardgameexpansion"
            val extensionsList =  XmlParserTask().execute(urlExtensions)
            progressBar.setProgress(50,true)
            if (boardgamesList.get().isEmpty()){
                Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show()

            }
            else{
                var id= 0

                for (boardgame in boardgamesList.get()){

                    db.addBoardGame(
                        Boardgame(
                            id = 0,
                            title = boardgame.title,
                            originalTitle = boardgame.originalTitle,
                            yearPublished = boardgame.yearPublished,
                            //te dwa ponizej do zmiany
                            image = boardgame.image,
                            thumbnail = boardgame.thumbnail,
                            bggId = boardgame.bggId


                        )
                    )
                    id+=1


                }
                progressBar.setProgress(75,true)
                for (extension in extensionsList.get()){
                    db.addExtension(
                        Boardgame(
                            id = 0,
                            title = extension.title,
                            originalTitle = extension.originalTitle,
                            yearPublished = extension.yearPublished,
                            //te dwa ponizej do zmiany
                            image = extension.image,
                            thumbnail = extension.thumbnail,
                            bggId = extension.bggId


                        )
                    )






                }


                db.close()
                progressBar.setProgress(100,true)
        }


// Ustaw maksymalną wartość



    }
}}