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
    // below method is for creating a database by running a sqlite query
    override fun onCreate(db: SQLiteDatabase) {

        val CREATE_TABLE_BOARDGAME = "CREATE TABLE IF NOT EXISTS boardgame " +
                "(id INTEGER  PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "original_title TEXT, " +
                "year_published INTEGER, " +
                "bgg_id INTEGER) "
        db?.execSQL(CREATE_TABLE_BOARDGAME)


        val CREATE_TABLE_EXTENSION =  "CREATE TABLE IF NOT EXISTS extension " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "original_title TEXT, " +
                "year_published INTEGER, " +
                "bgg_id INTEGER) "
        db?.execSQL(CREATE_TABLE_EXTENSION)


    }



    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " +"extension")
        onCreate(db)
    }
    fun addBoardGame(boardgame: Boardgame){
        val values = ContentValues()
        values.put("title", boardgame.title)

        values.put("bgg_id", boardgame.bggId)
        values.put("original_title", boardgame.originalTitle)
        values.put("year_published",boardgame.yearPublished)
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
            //zmienic image i thumbnail
            val boardGame = Boardgame(id, title, originalTitle, yearPublished," "," ", bggId)
            boardGames.add(boardGame)
        }
        cursor.close()
        return boardGames
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
            //zmienic image i thumbnail
            val boardGame = Boardgame(id, title, originalTitle, yearPublished," "," ", bggId)
            extensions.add(boardGame)
        }
        cursor.close()
        return extensions
    }


}