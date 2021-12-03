package com.example.android.october2021.sessions

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.october2021.db.GymRepository
import com.example.android.october2021.db.entities.*
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
    private val sessionWithSessionExercises = MutableLiveData<SessionWithSessionExercises>()
    val session = MutableLiveData<Session>() // holds info about the Session
    val sessionExerciseList = MutableLiveData<List<SessionExerciseWithExercise>>()


    // useless?
    val exercises = MutableLiveData<List<Exercise>>()
    val textInput = MutableLiveData<String>()

    private val _navigateToHome = MutableLiveData<Long>()
    val navigateToHome
        get() = _navigateToHome


    init {
        Log.d("SWM", "Argument Session ID is $sessionId")
        activeSessionId.value = sessionId
        initializeSession()
    }

    private fun initializeSession() {
        uiScope.launch {
            // Create new Session if none was submitted
            if (sessionId < 0) {
                activeSessionId.value = addNewSession().sessionId
            }

            // Get SessionWithSessionExercise from database using activeSessionId
            sessionWithSessionExercises.value = withContext(Dispatchers.IO){
                database.getSessionWithSessionExercises(activeSessionId.value!!)
            }
            // extract session info and list of SessionExercises from SessionWithSessionExercise
            session.value = withContext(Dispatchers.IO){ sessionWithSessionExercises.value!!.session }
            sessionExerciseList.value = withContext(Dispatchers.IO){sessionWithSessionExercises.value!!.sessionExercises }
            updateSessionExerciseList()

            Log.d("SVM", sessionWithSessionExercises.value.toString())

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
                Log.d("SWM", "session end time set")
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
        // TODO: update Session Workouts function

        sessionExerciseList.value = withContext(Dispatchers.IO) {
            database.getSessionWithSessionExercises(activeSessionId.value!!).sessionExercises
        }

        Log.d("SVM","exercise list updated")
    }

    private suspend fun addExerciseToSession() {
        withContext(Dispatchers.IO) {
            // TODO: implement functionality to associate a workout with a session
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