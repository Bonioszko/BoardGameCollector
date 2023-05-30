package edu.put.inf151892

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import models.Boardgame
import models.Boardgame_RecylerViewAdapter

class DlcActivity : AppCompatActivity() {

        val listGames: MutableList<Boardgame> = mutableListOf()
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_list_games)
            val db = DBHandler(this)
            var listOfExtensions = db.getAllExtensions()
            //val url ="https://boardgamegeek.com/xmlapi2/collection?username=rahdo"
            //var text = XmlParserTask().execute(url)
            val recyclerview = findViewById<RecyclerView>(R.id.RecyclerViewGames)
            recyclerview.layoutManager = LinearLayoutManager(this)
            val adapter = Boardgame_RecylerViewAdapter(listOfExtensions)
            recyclerview.adapter = adapter

    }
}