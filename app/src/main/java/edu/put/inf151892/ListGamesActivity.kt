package edu.put.inf151892

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Xml
import android.widget.TextView
import models.Boardgame

import models.XmlParserTask
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory


class ListGamesActivity : AppCompatActivity() {
    private lateinit var Text: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list_games)
        Text = findViewById(R.id.textView2)
        val url ="https://boardgamegeek.com/xmlapi2/collection?username=rahdo"
        var text = XmlParserTask().execute(url)





        Text.text = text.get()[1].title


    }}

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

