package models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.put.inf151892.R

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class Boardgame_RecylerViewAdapter(private val boardgameList: List<Boardgame>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mListener:onItemClickListener
    interface  onItemClickListener{
        fun onItemClick(position: Int){

        }
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_games, parent, false)
        return ViewHolder(view,mListener)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val boardgame = boardgameList[position]
        val viewHolder = holder as ViewHolder

        // Bind the data to the views in the ViewHolder
        Glide.with(viewHolder.itemView)
            .load(boardgame.thumbnail)
            .apply(RequestOptions().centerCrop())
            .into(viewHolder.imageView)
        viewHolder.numberInListView.text = (position+1).toString()
        viewHolder.titleGameView.text = boardgame.title.toString()
        viewHolder.yearOfPub.text = boardgame.yearPublished.toString()

    }

    override fun getItemCount(): Int{
        return boardgameList.size
    }
    class ViewHolder(ItemView:View, listener: onItemClickListener):RecyclerView.ViewHolder(ItemView){
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val numberInListView: TextView = itemView.findViewById(R.id.number_in_list)
        val titleGameView: TextView = itemView.findViewById(R.id.title_game)
        val yearOfPub: TextView = itemView.findViewById(R.id.year_of_pub)

        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}