package com.example.android.october2021.home

import android.app.Application
import androidx.lifecycle.*
import com.example.android.october2021.db.GymDatabaseDAO
import kotlinx.coroutines.*

class HomeViewModel(val database: GymDatabaseDAO, application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //val navigateToWorkouts = MutableLiveData<Boolean>()
    val sessions = database.getAllSessions()

    private val _navigateToWorkouts = MutableLiveData<Long>()
    val navigateToWorkouts
        get() = _navigateToWorkouts

    private val _navigateToSession = MutableLiveData<Long>()
    val navigateToSession
        get() = _navigateToSession

    init {
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onWorkoutsClicked() {
        navigateToWorkouts.value = 1
    }

    fun onSessionClicked(sessionId: Long) {
        _navigateToSession.value = sessionId
    }

    fun onWorkoutsNavigated() {
        navigateToWorkouts.value = -1
    }
    fun onSessionNavigated(){
        _navigateToSession.value = null
    }

    fun onNewSessionClicked() {
        navigateToSession.value = -1
    }

    fun onClearSessions() {
        uiScope.launch {
            clearSessionsDatabase()
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