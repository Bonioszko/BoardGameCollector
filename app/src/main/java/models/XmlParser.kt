package models
import android.os.AsyncTask
import android.util.Log
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
class XmlParserTask : AsyncTask<String, Void, List<Boardgame>>() {

    override fun doInBackground(vararg urls: String?): List<Boardgame> {
        val url = URL(urls[0])
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.readTimeout = 10000
        connection.connectTimeout = 15000
        connection.requestMethod = "GET"
        connection.doInput = true

        try {
            connection.connect()
            return parseXml(connection.inputStream)
        } catch (e: Exception) {
            Log.e("XmlParserTask", "Error during parsing", e)
        } finally {
            connection.disconnect()
        }
        return emptyList()
    }

    private fun parseXml(inputStream: InputStream): List<Boardgame> {
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = docBuilder.parse(inputStream)
        doc.documentElement.normalize()

        val itemList = doc.getElementsByTagName("item")
        val boardGames = mutableListOf<Boardgame>()
        for (i in 0 until itemList.length) {
            val itemNode = itemList.item(i)
            if (itemNode.nodeType == org.w3c.dom.Node.ELEMENT_NODE) {
                val elem = itemNode as org.w3c.dom.Element
                val id = elem.getAttribute("objectid").toInt()
                val nameElement = elem.getElementsByTagName("name").item(0)
                val name = nameElement?.textContent ?: ""
                val game = elem.getElementsByTagName("stats").item(0) as org.w3c.dom.Element
                val yearPublishedElement = elem.getElementsByTagName("yearpublished").item(0)
                val yearPublished = yearPublishedElement?.textContent?.toIntOrNull() ?: 0
                val imageElement = elem.getElementsByTagName("image").item(0)
                val image = imageElement?.textContent ?: ""
                val thumbnailElement = elem.getElementsByTagName("thumbnail").item(0)
                val thumbnail = thumbnailElement?.textContent ?: ""
                val minPlayers = game.getAttribute("minplayers").toIntOrNull() ?: 0
                val maxPlayers = game.getAttribute("maxplayers").toIntOrNull() ?: 0
                val playingTime = game.getAttribute("playingtime").toIntOrNull() ?: 0
                val boardGame = Boardgame(
                    id,
                    name,
                    name,
                    yearPublished,
                    image,
                    thumbnail,
                    id,
                    minPlayers,
                    maxPlayers,
                    playingTime,
                )
                boardGames.add(boardGame)
            }
        }
        return boardGames
    }
}


