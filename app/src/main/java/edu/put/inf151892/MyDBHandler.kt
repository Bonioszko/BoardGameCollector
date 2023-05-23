package edu.put.inf151892

import android.content.ContentValues
import android.content.Context
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
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "original_title TEXT, " +
                "year_published INTEGER, " +
                "bgg_id INTEGER) "
        db?.execSQL(CREATE_TABLE_BOARDGAME)


    }



    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }
    fun addBoardGame(boardgame: Boardgame){
        val values = ContentValues()
        values.put("title", boardgame.title)
        values.put("id", boardgame.id)
        values.put("bgg_id", boardgame.bggId)
        values.put("originalTitle", boardgame.originalTitle)
        values.put("year_published",boardgame.yearPublished)
        val db = this.writableDatabase
        db.insert("boardgame",null,values)
        db.close()
    }
    fun deleteAllBoardGames(){
        val db = this.writableDatabase
        db.delete("boardgame",null,null)
    }



}