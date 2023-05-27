package models
import android.os.AsyncTask
import android.util.Log
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

//class XmlParser {
//
//
//    fun parseXML(xmlString: String): List<Boardgame> {
//        val factory = DocumentBuilderFactory.newInstance()
//        val builder = factory.newDocumentBuilder()
//        val document = builder.parse(xmlString)
//
//        document.documentElement.normalize()
//
//        val rootElement = document.documentElement
//        val boardGameElements = rootElement.getElementsByTagName("item")
//
//        val boardGames = mutableListOf<Boardgame>()
//        for (i in 0 until boardGameElements.length) {
//            val boardGameElement = boardGameElements.item(i) as Element
//            val boardGame = processElement(boardGameElement)
//            boardGames.add(boardGame)
//        }
//
//        return boardGames
//    }
//
//    fun processElement(element: Element): Boardgame {
//
//
//        val nameElement = element.getElementsByTagName("name").item(0)
//        val name = nameElement?.textContent ?: ""
//
//        val yearPublishedElement = element.getElementsByTagName("yearpublished").item(0)
//        val yearPublished = yearPublishedElement?.textContent?.toIntOrNull() ?: 0
//
//        val imageElement = element.getElementsByTagName("image").item(0)
//        val image = imageElement?.textContent ?: ""
//
//        val thumbnailElement = element.getElementsByTagName("thumbnail").item(0)
//        val thumbnail = thumbnailElement?.textContent ?: ""
//
//        val id = element.getAttribute("objectid").toInt()
//
//        return Boardgame(id,name,name,yearPublished,image,thumbnail, id)
//    }
//    }
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

                val nameElement = elem.getElementsByTagName("name").item(0)
                val name = nameElement?.textContent ?: ""

                val yearPublishedElement = elem.getElementsByTagName("yearpublished").item(0)
                val yearPublished = yearPublishedElement?.textContent?.toIntOrNull() ?: 0

                val imageElement = elem.getElementsByTagName("image").item(0)
                val image = imageElement?.textContent ?: ""

                val thumbnailElement = elem.getElementsByTagName("thumbnail").item(0)
                val thumbnail = thumbnailElement?.textContent ?: ""

                val boardGame = Boardgame(
                    elem.getAttribute("objectid").toInt(),
                    name,
                    name,
                    yearPublished,
                    image,
                    thumbnail,
                    elem.getAttribute("objectid").toInt(),

                )
                boardGames.add(boardGame)
            }
        }
        return boardGames
    }
}


