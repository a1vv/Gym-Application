package com.example.android.october2021.sessions

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.october2021.db.GymRepository
import com.example.android.october2021.db.entities.Session
import com.example.android.october2021.db.entities.SessionExercise
import com.example.android.october2021.db.entities.SessionExerciseWithExercise
import kotlinx.coroutines.*

class SessionViewModel(
    private val database: GymRepository,
    application: Application,
    private var sessionId: Long
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val session = MutableLiveData<Session>() // holds info about the Session to be accessed by binding
    val sessionExerciseList = MutableLiveData<List<SessionExerciseWithExercise>>()

    val exerciseId = MutableLiveData<Long>()

    private val _navigateToExercisePicker = MutableLiveData<Long>()
    val navigateToExercisePicker
        get() = _navigateToExercisePicker


    init {
        Log.d("SVM", "INIT: argSessionId is $sessionId")
        initializeSession()
    }

    private fun initializeSession() {
        uiScope.launch {
            updateSessionExerciseList()
            updateSession()
        }
    }


    fun updateSession(argSessionId : Long) {
        sessionId = argSessionId
        initializeSession()
    }

    private suspend fun updateSession() {
        session.value = withContext(Dispatchers.IO) {
            Log.d("SVM","updateSession with sessionId: $sessionId")
            database.getSession(sessionId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onExerciseAdded(exerciseId: Long) {
        if (exerciseId > -1) {
            uiScope.launch {
                Log.d("SVM", "eID: $exerciseId, sID: $sessionId")
                addExerciseToSession(exerciseId)
                updateSessionExerciseList()
            }
        }
    }

    /**
     * Starts the navigation to ExercisePickerFragment
     */
    fun onAddExerciseClicked() {
        uiScope.launch {
            navigateToExercisePicker.value = sessionId
            updateSessionExerciseList()
        }
    }

    /**
     * Set variable back to null after navigation
     */
    fun onExercisePickerNavigated() {
        _navigateToExercisePicker.value = null
    }

    /**
     * Update the list of SessionExercises from the database
     */
    private suspend fun updateSessionExerciseList() {
        sessionExerciseList.value = withContext(Dispatchers.IO) {
            database.getSessionExercises(sessionId)
        }
        Log.d("SVM", "exercise list updated")
    }

    /**
     * Create a new SessionExercise with the right sessionId and exerciseId
     */
    private suspend fun addExerciseToSession(exerciseId: Long) {
        withContext(Dispatchers.IO) {
            Log.d("SVM", "SessionExercises created with sID: $sessionId, eID: $exerciseId")
            database.insertSessionExercise(
                SessionExercise(
                    0,
                    "Test",
                    sessionId,
                    exerciseId
                )
            )
        }
        Log.d("SVM", "SessionExercise added to session")
        Log.d("SVM", sessionExerciseList.value.toString())
    }

    /**
     * Handle press-events on SessionExercise items
     */
    fun onSessionExerciseClicked(sessionExerciseId: Long) {
        Log.d("SVM", sessionExerciseId.toString())
    }

    class SessionViewModelFactory(
        private val dataSource: GymRepository,
        private val application: Application,
        private val sessionId: Long
    ) : ViewModelProvider.Factory {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SessionViewModel::class.java)) {
                return SessionViewModel(dataSource, application, sessionId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}