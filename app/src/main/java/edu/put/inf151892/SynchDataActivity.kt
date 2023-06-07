package edu.put.inf151892

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import models.Boardgame
import models.XmlParserTask
import java.time.Instant
import java.time.LocalDate

class SynchDataActivity : AppCompatActivity() {

    private  lateinit var synchText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_synch_data)
        var done = false
        val cache = getSharedPreferences("cache", MODE_PRIVATE)
        val progressBar: ProgressBar = findViewById(R.id.progressBar) // Znajdź ProgressBar na podstawie ID
        progressBar.progress = 0
        progressBar.max = 100
        synchText = findViewById(R.id.synchText)
        synchText.text = "Last synchronised: " + cache.getString("syncDate", "")
        val lastSyncLong = cache.getLong("syncDateLong", Instant.now().toEpochMilli())
        val syncButton :Button = findViewById(R.id.sync)
        syncButton.setOnClickListener(){
            if (lastSyncLong < Instant.now().minusSeconds(86400).toEpochMilli()) {
                val db = DBHandler(this)
                cache.edit().putString("synchDate", LocalDate.now().toString())
                db.deleteAllExtensions()
                db.deleteAllBoardGames()
                var url =
                    "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString(
                        "username",
                        ""
                    ) +
                            "&subtype=boardgame&excludesubtype=boardgameexpansion&stats=1"
                cache.getString("username", "")?.let { it1 -> Log.d("tag", it1) }
                val boardgamesList = XmlParserTask().execute(url)
                progressBar.setProgress(25, true)
                var urlExtensions =
                    "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString(
                        "username",
                        ""
                    ) +
                            "&subtype=boardgameexpansion&stats=1"
                val extensionsList = XmlParserTask().execute(urlExtensions)
                progressBar.setProgress(50, true)
                if (boardgamesList.get().isEmpty()) {
                    Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show()

                } else {


                    for (boardgame in boardgamesList.get()) {

                        db.addBoardGame(
                            Boardgame(
                                id = 0,
                                title = boardgame.title,
                                originalTitle = boardgame.originalTitle,
                                yearPublished = boardgame.yearPublished,
                                //te dwa ponizej do zmiany
                                image = boardgame.image,
                                thumbnail = boardgame.thumbnail,
                                bggId = boardgame.bggId,
                                minPlayers = boardgame.minPlayers,
                                maxPlayers = boardgame.maxPlayers,
                                playingTime = boardgame.playingTime


                            )
                        )


                    }
                    progressBar.setProgress(75, true)
                    for (extension in extensionsList.get()) {
                        db.addExtension(
                            Boardgame(
                                id = 0,
                                title = extension.title,
                                originalTitle = extension.originalTitle,
                                yearPublished = extension.yearPublished,
                                //te dwa ponizej do zmiany
                                image = extension.image,
                                thumbnail = extension.thumbnail,
                                bggId = extension.bggId,
                                minPlayers = extension.minPlayers,
                                maxPlayers = extension.maxPlayers,
                                playingTime = extension.playingTime


                            )
                        )


                    }


                    db.close()
                    progressBar.setProgress(100, true)
                    Toast.makeText(this, "SYNCHRONIZATION DONE", Toast.LENGTH_SHORT).show()

                }
                done = false
            }
            else if(done == true ) {val db = DBHandler(this)
                cache.edit().putString("synchDate", LocalDate.now().toString())
                db.deleteAllExtensions()
                db.deleteAllBoardGames()
                var url =
                    "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString(
                        "username",
                        ""
                    ) +
                            "&subtype=boardgame&excludesubtype=boardgameexpansion&stats=1"
                cache.getString("username", "")?.let { it1 -> Log.d("tag", it1) }
                val boardgamesList = XmlParserTask().execute(url)
                progressBar.setProgress(25, true)
                var urlExtensions =
                    "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString(
                        "username",
                        ""
                    ) +
                            "&subtype=boardgameexpansion&stats=1"
                val extensionsList = XmlParserTask().execute(urlExtensions)
                progressBar.setProgress(50, true)
                if (boardgamesList.get().isEmpty()) {
                    Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show()

                } else {


                    for (boardgame in boardgamesList.get()) {

                        db.addBoardGame(
                            Boardgame(
                                id = 0,
                                title = boardgame.title,
                                originalTitle = boardgame.originalTitle,
                                yearPublished = boardgame.yearPublished,
                                //te dwa ponizej do zmiany
                                image = boardgame.image,
                                thumbnail = boardgame.thumbnail,
                                bggId = boardgame.bggId,
                                minPlayers = boardgame.minPlayers,
                                maxPlayers = boardgame.maxPlayers,
                                playingTime = boardgame.playingTime


                            )
                        )


                    }
                    progressBar.setProgress(75, true)
                    for (extension in extensionsList.get()) {
                        db.addExtension(
                            Boardgame(
                                id = 0,
                                title = extension.title,
                                originalTitle = extension.originalTitle,
                                yearPublished = extension.yearPublished,
                                //te dwa ponizej do zmiany
                                image = extension.image,
                                thumbnail = extension.thumbnail,
                                bggId = extension.bggId,
                                minPlayers = extension.minPlayers,
                                maxPlayers = extension.maxPlayers,
                                playingTime = extension.playingTime


                            )
                        )


                    }


                    db.close()
                    progressBar.setProgress(100, true)
                    Toast.makeText(this, "SYNCHRONIZATION DONE", Toast.LENGTH_SHORT).show()

                }
                done = false

            }
            else {
                progressBar.setProgress(0, true)
                done = true
                Toast.makeText(this, "Data is already synchronised!, approve this operation", Toast.LENGTH_SHORT).show()
            }

// Ustaw maksymalną wartość



    }
}}