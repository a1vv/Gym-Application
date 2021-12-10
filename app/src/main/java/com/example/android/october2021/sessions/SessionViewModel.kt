package com.example.android.october2021.sessions

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.october2021.db.GymRepository
import com.example.android.october2021.db.entities.Exercise
import com.example.android.october2021.db.entities.Session
import com.example.android.october2021.db.entities.SessionExercise
import com.example.android.october2021.db.entities.SessionExerciseWithExercise
import kotlinx.coroutines.*

class SessionViewModel(
    private val database: GymRepository,
    application: Application,
    private var sessionId: Long
) : AndroidViewModel(application) {

    // vital
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val activeSessionId = MutableLiveData<Long>()
    val session = MutableLiveData<Session>() // holds info about the Session
    val sessionExerciseList = MutableLiveData<List<SessionExerciseWithExercise>>()


    // useless?
    val exercises = MutableLiveData<List<Exercise>>()
    val textInput = MutableLiveData<String>()

    private val _navigateToHome = MutableLiveData<Long>()
    val navigateToHome
        get() = _navigateToHome


    init {
        Log.d("SVM", "Argument Session ID is $sessionId")
        activeSessionId.value = sessionId
        initializeSession()
    }

    private fun initializeSession() {
        uiScope.launch {
            // Create new Session if none was submitted
            if (sessionId < 0) {
                activeSessionId.value = addNewSession().sessionId
            }

            // extract session info and list of SessionExercises from SessionWithSessionExercise
            session.value = withContext(Dispatchers.IO){ database.getSession(activeSessionId.value!!)}
            sessionExerciseList.value = withContext(Dispatchers.IO){ database.getSessionExercises(activeSessionId.value!!) }


            Log.d("SVM", sessionExerciseList.value.toString())
            Log.d("SVM", session.value.toString())

            updateSessionExerciseList()


        }
    }

    private suspend fun addNewSession(): Session {
        return withContext(Dispatchers.IO) {
            database.insertSession(Session())
            database.getLastSession()!!
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onSubmitSession() {
        uiScope.launch {
            changeSessionEndTime()
        }
        _navigateToHome.value = 1
    }

    private suspend fun changeSessionEndTime() {
        withContext(Dispatchers.IO) {
            val session = database.getSession(activeSessionId.value!!)
            val currentSession = database.getLastSession()
            if (currentSession!! == session) {
                session.endTimeMilli = System.currentTimeMillis()
                database.updateSession(session)
                Log.d("SVM", "session end time set")
            }
        }
    }

    fun onAddExerciseClicked() {
        uiScope.launch {
            addExerciseToSession()
            updateSessionExerciseList()
            textInput.value = ""
        }
    }

    private suspend fun updateSessionExerciseList() {
        sessionExerciseList.value = withContext(Dispatchers.IO) {
            database.getSessionExercises( activeSessionId.value!!)
        }
        Log.d("SVM","exercise list updated")
    }

    private suspend fun addExerciseToSession() {
        withContext(Dispatchers.IO) {
            database.insertSessionExercise(
                SessionExercise(
                    0,
                    "Test",
                    activeSessionId.value!!,
                    database.getLastExercise()!!.exerciseId
                )
            )
        }
        Log.d("SVM","SessionExercise added to session")

        Log.d("SVM",sessionExerciseList.value.toString())
    }

    fun onHomeNavigated() {
        _navigateToHome.value = null
    }

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