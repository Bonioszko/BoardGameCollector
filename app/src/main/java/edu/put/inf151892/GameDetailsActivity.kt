package edu.put.inf151892

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
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details )
        text= findViewById(R.id.description)
        image = findViewById(R.id.image)
        val bundle : Bundle? = intent.extras
        val bggId = bundle!!.getInt("bggId")

        CoroutineScope(Dispatchers.Main).launch {
            val boardGameDetails = fetchBoardGameDetails(bggId.toString())
            text.text = boardGameDetails.description
            Glide.with(this@GameDetailsActivity)
                .load(boardGameDetails.image)
                .apply(RequestOptions().centerCrop())
                .into(image)
            // Now you can use the fetched board game details as needed
            // For example, you can access properties like boardGameDetails.name, boardGameDetails.thumbnail, etc.
        }

    }



    suspend fun fetchBoardGameDetails(gameId: String): BoardGameDetails {
        val boardGameDetailsParser = BoardGameDetailsParser(gameId)
        return boardGameDetailsParser.parse()
    }

}