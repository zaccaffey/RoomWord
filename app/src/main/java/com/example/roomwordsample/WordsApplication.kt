package com.example.roomwordsample

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WordsApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())
    // create a database instance
    // (we use lazy because these objects should only be created when theyre first needed
    val database by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
    // create a repo instance based on the DAO
    val repository by lazy { WordRepository(database.wordDao()) }
}