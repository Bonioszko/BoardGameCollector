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

//    private fun parseXml(xmlUrl:String): List<Boardgame> {
//
//        val xmlString = getXmlString(xmlUrl)
//
//        // Parse the XML string
//        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
//        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
//        val document = documentBuilder.parse(InputSource(StringReader(xmlString)))
//        Log.d("main ")
//        val itemList = document.getElementsByTagName("item")
//        val boardGames = mutableListOf<Boardgame>()
//        for (i in 0 until itemList.length) {
//            val itemNode = itemList.item(i)
//            if (itemNode.nodeType == org.w3c.dom.Node.ELEMENT_NODE) {
//                val elem = itemNode as org.w3c.dom.Element
//
//                val nameElement = elem.getElementsByTagName("name").item(0)
//                val name = nameElement?.textContent ?: ""
//
//                val yearPublishedElement = elem.getElementsByTagName("yearpublished").item(0)
//                val yearPublished = yearPublishedElement?.textContent?.toIntOrNull() ?: 0
//
//                val imageElement = elem.getElementsByTagName("image").item(0)
//                val image = imageElement?.textContent ?: ""
//
//                val thumbnailElement = elem.getElementsByTagName("thumbnail").item(0)
//                val thumbnail = thumbnailElement?.textContent ?: ""
//                val id = elem.getAttribute("objectid").toInt()
//                val boardGame = Boardgame(
//                    elem.getAttribute("objectid").toInt(),
//                    name,
//                    name,
//                    yearPublished,
//                    image,
//                    thumbnail,
//                    id
//                )
//                boardGames.add(boardGame)
//
//
//            }
//
//
//        }
//        return boardGames
//    }
//    fun getXmlString(url: String): String {
//        val connection = URL(url).openConnection() as HttpURLConnection
//        connection.requestMethod = "GET"
//        connection.connect()
//
//        val responseCode = connection.responseCode
//        return if (responseCode == HttpURLConnection.HTTP_OK) {
//            val reader = BufferedReader(InputStreamReader(connection.inputStream))
//            val xmlStringBuilder = StringBuilder()
//            var line: String? = reader.readLine()
//            while (line != null) {
//                xmlStringBuilder.append(line)
//                line = reader.readLine()
//            }
//            reader.close()
//            xmlStringBuilder.toString()
//        } else {
//            throw IOException("Failed to fetch XML data. Response code: $responseCode")
//        }
//    }
//}

