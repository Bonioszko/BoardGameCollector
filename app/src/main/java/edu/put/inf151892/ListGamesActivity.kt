package edu.put.inf151892

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import models.Boardgame
import models.Boardgame_RecylerViewAdapter


class ListGamesActivity : AppCompatActivity() {

    val listGames: MutableList<Boardgame> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list_games)
        val db = DBHandler(this)
        var listOfGames = db.getAllBoardGames()
        //val url ="https://boardgamegeek.com/xmlapi2/collection?username=rahdo"
        //var text = XmlParserTask().execute(url)
        val recyclerview = findViewById<RecyclerView>(R.id.RecyclerViewGames)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = Boardgame_RecylerViewAdapter(listOfGames)
        recyclerview.adapter = adapter
        adapter.setOnItemClickListener(object : Boardgame_RecylerViewAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@ListGamesActivity, GameDetailsActivity::class.java)
                intent.putExtra("bggId",listOfGames[position].bggId )
                intent.putExtra("image", listOfGames[position].image)
                intent.putExtra("name", listOfGames[position].title)
                intent.putExtra("minPlayers", listOfGames[position].minPlayers)
                intent.putExtra("maxPlayers", listOfGames[position].maxPlayers)
                intent.putExtra("yearPublished", listOfGames[position].yearPublished)
                intent.putExtra("playingTime", listOfGames[position].playingTime)
                startActivity(intent)
            }
        })
    }


}



