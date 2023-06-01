package edu.put.inf151892

import android.media.AsyncPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import models.BoardGameDetails
import models.BoardGameDetailsParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class GameDetailsActivity : AppCompatActivity() {
    private lateinit var text:TextView
    private lateinit var image: ImageView
    private lateinit var numberOfPlayers: TextView
    private lateinit var released: TextView
    private lateinit var playingTime: TextView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details )
        text= findViewById(R.id.titleText)
        image = findViewById(R.id.imageView)
        numberOfPlayers = findViewById(R.id.numberOfPlayers)
        released = findViewById(R.id.released)
        playingTime = findViewById(R.id.playingTime)
        val bundle : Bundle? = intent.extras
        val bggId = bundle!!.getInt("bggId")
        val imageBundle = bundle.getString("image")
        val playingTimeInt = bundle.getInt("playingTime")
        val minPlayers = bundle.getInt("minPlayers")
        val maxPlayers = bundle.getInt("maxPlayers")
        val yearPublished = bundle.getInt("yearPublished")
        val name = bundle.getString("name")
        text.text = name
        Glide.with(this).load(imageBundle)
            .apply(RequestOptions()
                .centerCrop())
            .into(image)
        numberOfPlayers.append(minPlayers.toString())
        numberOfPlayers.append(" - ")
        numberOfPlayers.append(maxPlayers.toString())
        released.append(yearPublished.toString())
        playingTime.append(playingTimeInt.toString())
        playingTime.append(" min")
//        CoroutineScope(Dispatchers.Main).launch {
//            val boardGameDetails = fetchBoardGameDetails(bggId.toString())
//            text.text = boardGameDetails.name
//            Glide.with(this@GameDetailsActivity)
//                .load(imageBundle)
//                .apply(RequestOptions().centerCrop())
//                .into(image)
//            numberOfPlayers.append(boardGameDetails.minPlayers)
//            numberOfPlayers.append(" - ")
//            numberOfPlayers.append(boardGameDetails.maxPlayers)
//            released.append(boardGameDetails.yearPublished)
//            playingTime.append(boardGameDetails.playingTime)
//            playingTime.append(" min")
//            // Now you can use the fetched board game details as needed
//            // For example, you can access properties like boardGameDetails.name, boardGameDetails.thumbnail, etc.
//        }

    }



    suspend fun fetchBoardGameDetails(gameId: String): BoardGameDetails {
        val boardGameDetailsParser = BoardGameDetailsParser(gameId)
        return boardGameDetailsParser.parse()
    }

}