package com.example.android.october2021.exercises

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.october2021.db.GymRepository
import com.example.android.october2021.db.entities.Exercise
import kotlinx.coroutines.*

class ExercisesViewModel(
    val database: GymRepository,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var currentExercise = MutableLiveData<Exercise?>()
    val exercises = database.getAllExercises()
    val textInput = MutableLiveData<String>()

    // if textInput has a value, the submit-button should be enabled
    val submitButtonVisible = Transformations.map(textInput){
        it.toString() != ""
    }

    // clear database button should be enabled when the list is not empty
    val clearButtonVisible = Transformations.map(exercises){
        //workouts.value?.size != 0
        true
    }

    init {
        initializeExercise()
    }

    private fun initializeExercise() {
        uiScope.launch {
            currentExercise.value = getCurrentExerciseFromDatabase()
        }
    }

    private suspend fun getCurrentExerciseFromDatabase(): Exercise? {
        return withContext(Dispatchers.IO) {
            val exercise = database.getLastExercise()
            exercise
        }
    }

    private suspend fun insert(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            database.insertExercise(exercise)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clearAllTables()
        }
    }


    fun onAddExercise() {
        viewModelScope.launch {
            // Create a new workout, which captures the current text, TOTO
            // and insert it into the database.
            Log.d("WVM", "Submit button clicked!!!")
            val newExercise = Exercise(0, textInput.value.toString())
            insert(newExercise)
            currentExercise.value = getCurrentExerciseFromDatabase()
            textInput.value = ""
        }
    }

    fun onClearExercises() {
        viewModelScope.launch {
            Log.d("WVM", "Clear button clicked!")
            clear()
            Log.d("WVM", "Database cleared")
        }
    }

}

class ExercisesViewModelFactory(
    private val dataSource: GymRepository,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExercisesViewModel::class.java)) {
            return ExercisesViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}