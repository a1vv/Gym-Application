package com.example.android.october2021.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.october2021.db.GymDatabaseDAO
import com.example.android.october2021.db.entities.Session
import kotlinx.coroutines.*

class HomeViewModel(val database: GymDatabaseDAO, application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //val navigateToWorkouts = MutableLiveData<Boolean>()
    val sessions = database.getAllSessions()

    private val _navigateToExercises = MutableLiveData<Long>()
    val navigateToExercises
        get() = _navigateToExercises

    private val _navigateToSession = MutableLiveData<Long>()
    val navigateToSession
        get() = _navigateToSession

    init {
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onExercisesClicked() {
        navigateToExercises.value = 1
    }

    fun onSessionClicked(sessionId: Long) {
        _navigateToSession.value = sessionId
    }

    fun onExercisesNavigated() {
        navigateToExercises.value = -1
    }
    fun onSessionNavigated(){
        _navigateToSession.value = null
    }

    fun onNewSessionClicked() {
        Log.d("HVM","newSession clicked.")
        uiScope.launch { navigateToSession.value = addNewSession() }

    }

    fun onClearSessions() {
        uiScope.launch {
            clearSessionsDatabase()
        }
    }

    /**
     * Adds a new session to database and returns its ID
     */
    private suspend fun addNewSession(): Long {
        return withContext(Dispatchers.IO) {
            Log.d("HVM","Session insertion")
            database.insertSession(Session())
        }
    }
    private suspend fun clearSessionsDatabase() {
        withContext(Dispatchers.IO) {
            database.clearSessions()
        }
    }
}

class HomeViewModelFactory(
    private val dataSource: GymDatabaseDAO,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}