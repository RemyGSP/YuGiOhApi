package com.example.recyclerviewpeliculas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

// Define contract class
// Contract class
object CardContract {
    object CardEntry : BaseColumns {
        const val TABLE_NAME = "favorite_cards"
        const val COLUMN_NAME_INDEX = "card_index"
    }
}

// Database helper class
class CardDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "card.db"
        const val DATABASE_VERSION = 1

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${CardContract.CardEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${CardContract.CardEntry.COLUMN_NAME_INDEX} INTEGER)"

        private const val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS ${CardContract.CardEntry.TABLE_NAME}"
    }
}

// Model class
data class YuGiOhCard(val index: Int)

// Database operations class
class CardDataSource(context: Context) {
    private val dbHelper: CardDBHelper = CardDBHelper(context)
    private lateinit var database: SQLiteDatabase

    fun open() {
        database = dbHelper.writableDatabase
    }

    fun close() {
        dbHelper.close()
    }

    fun addFavoriteCard(cardIndex: Int): Long {
        val values = ContentValues().apply {
            put(CardContract.CardEntry.COLUMN_NAME_INDEX, cardIndex)
        }
        return database.insert(CardContract.CardEntry.TABLE_NAME, null, values)
    }

    fun removeFavoriteCard(cardIndex: Int) {
        database.delete(
            CardContract.CardEntry.TABLE_NAME,
            "${CardContract.CardEntry.COLUMN_NAME_INDEX} = ?",
            arrayOf(cardIndex.toString())
        )
    }

    private val allCards: List<Card> = mutableListOf() // Initialize with all cards

    fun getAllFavoriteCards(): List<Int> {
        val favoriteCards = mutableListOf<Int>()
        val cursor = database.query(
            CardContract.CardEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                val cardIndex = it.getInt(it.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_NAME_INDEX))
                    favoriteCards.add(cardIndex)

            }
        }
        return favoriteCards
    }

}