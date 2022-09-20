package com.example.roomwordsample

import android.content.Context
import androidx.room.CoroutinesRoom
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Word::class], version = 1, exportSchema = false)
public abstract class WordRoomDatabase: RoomDatabase() {
    abstract fun wordDao(): WordDao

    // using singleton here prevents more than one instance of the database being opened at the same time
    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        // this returns the singleton and creates the database the first time it is accessed
        fun getDatabase(context: Context, scope: CoroutineScope): WordRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database")
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // this is used to delete all content and repopulate the database whenever the app is created
    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            // Delete all content here.
            wordDao.deleteAll()

            // Add sample words.
            var word = Word(1, "Hello")
            wordDao.insert(word)
            word = Word(2, "World!")
            wordDao.insert(word)
            word = Word(3, "Healthy")
            wordDao.insert(word)
            word = Word(4, "Calm")
            wordDao.insert(word)
        }
    }
}