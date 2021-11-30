package com.example.android.october2021.sessions

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.room.RoomWarnings
import com.example.android.october2021.db.GymRepository
import com.example.android.october2021.db.entities.*
import kotlinx.coroutines.*

class SessionViewModel(
    private val database: GymRepository,
    application: Application,
    private var sessionId: Long
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val activeSessionId = MutableLiveData<Long>()

    val session = MutableLiveData<Session?>()
    val textInput = MutableLiveData<String>()

    val exercises = MutableLiveData<List<Exercise>>()
    val sessionExerciseList = MutableLiveData<List<SessionExercise>>()
    val sessionExercise = MutableLiveData<SessionExercise?>()


    private val _navigateToHome = MutableLiveData<Long>()
    val navigateToHome
        get() = _navigateToHome


    init {
        initializeSession()
    }

    private fun initializeSession() {
        Log.d("SWM", "Argument Session ID is $sessionId")
        activeSessionId.value = sessionId
        uiScope.launch {
            if (sessionId < 0) {
                activeSessionId.value = addNewSession().sessionId
            }
            session.value = getSessionInfoFromDatabase()
            updateSessionWorkouts()

        }
        prepareSpinner()
    }

    private fun prepareSpinner() {
        exercises.value = database.getAllExercises().value
        //TODO MAKES NO SENSE, EXERCISES.VALUE GETS SET TO NULL????
        Log.d("SWM", "spinner prepared ${exercises.value}")
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

    private suspend fun getSessionInfoFromDatabase(): Session? {
        return withContext(Dispatchers.IO) {
            val session = database.getSessionWithId(activeSessionId.value!!)
            session
        }
    }

    private suspend fun changeSessionEndTime() {
        withContext(Dispatchers.IO) {
            val session = database.getSessionWithId(activeSessionId.value!!)
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
            prepareSpinner()
            addExerciseToSession()
            updateSessionWorkouts()
            textInput.value = ""
        }
    }

    private suspend fun updateSessionWorkouts() {
        sessionExerciseList.value = withContext(Dispatchers.IO) {
            database.getSessionWithSessionExercises(activeSessionId.value!!)
        }
    }

    private suspend fun addExerciseToSession() {
        withContext(Dispatchers.IO) {
            // TODO: implement functionality to associate  a workout with a session
            val exerciseId = database.getLastExercise()!!.exerciseId


            // create a new sessionExercise, connected to the current sessionId and the selected exerciseId
            val sessionExercise = SessionExercise(
                0,
                "text",
                activeSessionId.value!!,
                exerciseId,
                database.getLastExercise()!!
            )
            database.insertSessionExercise(sessionExercise)
        }
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