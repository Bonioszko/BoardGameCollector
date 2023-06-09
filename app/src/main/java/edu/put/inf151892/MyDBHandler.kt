package edu.put.inf151892

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import models.Boardgame

class DBHandler  // creating a constructor for our database handler.
    (context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        // creating a constant variables for our database.
        // below variable is for our database name.
        private const val DB_NAME = "boardgames.db"

        // below int is our database version
        private const val DB_VERSION = 1

        // below variable is for our table name.
        private const val TABLE_NAME = "boardgames"



    }

    override fun onCreate(db: SQLiteDatabase) {

        val CREATE_TABLE_BOARDGAME = "CREATE TABLE IF NOT EXISTS boardgame " +
                "(id INTEGER  PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "original_title TEXT, " +
                "year_published INTEGER, " +
                "image TEXT,"+
                "thumbnail TEXT,"+
                "bgg_id INTEGER," +
                "min_players INTEGER," +
                "max_players INTEGER," +
                "playing_time INTEGER) "
        db?.execSQL(CREATE_TABLE_BOARDGAME)


        val CREATE_TABLE_EXTENSION =  "CREATE TABLE IF NOT EXISTS extension " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "original_title TEXT, " +
                "year_published INTEGER, " +
                "image TEXT,"+
                "thumbnail TEXT,"+
                "bgg_id INTEGER," +
                "min_players INTEGER," +
                "max_players INTEGER," +
                "playing_time INTEGER) "
        db?.execSQL(CREATE_TABLE_EXTENSION)



     val CREATE_TABLE_IMAGES =  "CREATE TABLE IF NOT EXISTS images" +
            "(bgg_id INTEGER," +
            " image TEXT)"

        db?.execSQL(CREATE_TABLE_IMAGES)

}



    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " +"extension")
        db.execSQL("DROP TABLE IF EXISTS " +"images")
        onCreate(db)
    }
    fun addImage( id: Int, image: String){
        val values = ContentValues()
        values.put("bgg_id", id)
        values.put("image", image)
        val db = this.writableDatabase
        db.insert("images",null,values)
        db.close()
    }

    fun addBoardGame(boardgame: Boardgame){
        val values = ContentValues()
        values.put("title", boardgame.title)

        values.put("bgg_id", boardgame.bggId)
        values.put("original_title", boardgame.originalTitle)
        values.put("year_published",boardgame.yearPublished)
        values.put("image", boardgame.image)
        values.put("thumbnail", boardgame.thumbnail)
        values.put("min_players", boardgame.minPlayers)
        values.put("max_players", boardgame.maxPlayers)
        values.put("playing_time", boardgame.playingTime)
        val db = this.writableDatabase
        db.insert("boardgame",null,values)
        db.close()
    }
    fun addExtension(extension: Boardgame){
        val values = ContentValues()
        values.put("title", extension.title)
        values.put("bgg_id", extension.bggId)
        values.put("original_title", extension.originalTitle)
        values.put("year_published",extension.yearPublished)
        values.put("image", extension.image)
        values.put("thumbnail", extension.thumbnail)
        values.put("min_players", extension.minPlayers)
        values.put("max_players", extension.maxPlayers)
        values.put("playing_time", extension.playingTime)
        val db = this.writableDatabase
        db.insert("extension",null,values)
        db.close()
    }
    fun deleteAllBoardGames(){
        val db = this.writableDatabase
        db.delete("boardgame",null,null)
    }
    fun deleteAllExtensions(){
        val db = this.writableDatabase
        db.delete("extension",null,null)
    }
    fun deleteAllImages(){
        val db = this.writableDatabase
        db.delete("images",null,null)
    }
    fun deleteImagesforGame(id: Int){
        val db = this.writableDatabase
        val whereClause = "bgg_id = ?"
        val whereArgs = arrayOf(id.toString())
        db.delete("images", whereClause, whereArgs)
        db.close()
    }
    @SuppressLint("Range")
    fun getAllBoardGames(): List<Boardgame> {
        val db = this.writableDatabase
        val boardGames = mutableListOf<Boardgame>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM boardgame", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val title = cursor.getString(cursor.getColumnIndex("title"))
            val originalTitle = cursor.getString(cursor.getColumnIndex("original_title"))
            val yearPublished = cursor.getInt(cursor.getColumnIndex("year_published"))
            val bggId = cursor.getInt(cursor.getColumnIndex("bgg_id"))
            val image = cursor.getString(cursor.getColumnIndex("image"))
            val thumbnail = cursor.getString(cursor.getColumnIndex("thumbnail"))
            val minPlayers = cursor.getInt(cursor.getColumnIndex("min_players"))
            val maxPlayers = cursor.getInt(cursor.getColumnIndex("max_players"))
            val playingTime = cursor.getInt(cursor.getColumnIndex("playing_time"))

            val boardGame = Boardgame(id, title, originalTitle, yearPublished,image, thumbnail , bggId, minPlayers,maxPlayers,playingTime)
            boardGames.add(boardGame)
        }
        cursor.close()
        return boardGames
    }

    @SuppressLint("Range")
    fun getImages(id: Int):MutableList<String>{
        val db = this.writableDatabase
        val images = mutableListOf<String>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM images where bgg_id = $id", null)
        while (cursor.moveToNext()){
            val title = cursor.getString(cursor.getColumnIndex("image"))
            images.add(title)
        }
        cursor.close()
        return images
    }
    @SuppressLint("Range")
    fun getAllExtensions(): List<Boardgame> {
        val db = this.writableDatabase
        val extensions = mutableListOf<Boardgame>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM extension", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val title = cursor.getString(cursor.getColumnIndex("title"))
            val originalTitle = cursor.getString(cursor.getColumnIndex("original_title"))
            val yearPublished = cursor.getInt(cursor.getColumnIndex("year_published"))
            val bggId = cursor.getInt(cursor.getColumnIndex("bgg_id"))
            val image = cursor.getString(cursor.getColumnIndex("image"))
            val thumbnail = cursor.getString(cursor.getColumnIndex("thumbnail"))
            val minPlayers = cursor.getInt(cursor.getColumnIndex("min_players"))
            val maxPlayers = cursor.getInt(cursor.getColumnIndex("max_players"))
            val playingTime = cursor.getInt(cursor.getColumnIndex("playing_time"))
            //zmienic image i thumbnail
            val boardGame = Boardgame(id, title, originalTitle, yearPublished,image,thumbnail, bggId,minPlayers,maxPlayers,playingTime)
            extensions.add(boardGame)
        }
        cursor.close()
        return extensions
    }
    fun numberOfDlc(): Int{
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT COUNT(*) FROM extension", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count

    }
    fun numberOfGames(): Int{
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT COUNT(*) FROM boardgame", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count

    }


}