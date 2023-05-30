package edu.put.inf151892


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.util.Xml
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import com.google.android.material.internal.ContextUtils.getActivity


import models.Boardgame
import models.XmlParserTask


class ConfigActivity : AppCompatActivity() {

    private lateinit var inputUsername: EditText
    private lateinit var usernameButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        val cache = getSharedPreferences("cache", Context.MODE_PRIVATE)

        inputUsername =findViewById(R.id.usernameText)
        usernameButton = findViewById(R.id.usernameButton)
        usernameButton.setOnClickListener{
            val username = inputUsername.text.toString()
            Log.d("mian",username)
            cache.edit().putString("username",username).apply()
            cache.edit().putBoolean("confDone",true).apply()
            val db = DBHandler(this)

            Log.d("msf",cache.getString("username", "").toString())
            var url  = "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString(
            "username",
            ""
        ) +
                "&subtype=boardgame&excludesubtype=boardgameexpansion"
            val boardgamesList =  XmlParserTask().execute(url)
            var urlExtensions  = "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString(
            "username",
            ""
        ) +
                "&subtype=boardgameexpansion"
            val extensionsList =  XmlParserTask().execute(urlExtensions)
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

                val extension =db.getAllExtensions()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }


        }






    }

}