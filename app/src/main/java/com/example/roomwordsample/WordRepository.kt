package com.example.roomwordsample

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

// This is our repository which will fetch data from the database using DAO and provide the data to the ViewModel
class WordRepository(private val wordDao: WordDao) {
    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}