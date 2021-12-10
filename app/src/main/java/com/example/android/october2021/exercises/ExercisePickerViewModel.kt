package com.example.android.october2021.exercises

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.october2021.db.GymRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ExercisePickerViewModel(
    val database: GymRepository,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val exercises = database.getAllExercises()


    // add private navigateToSession with public getter
    private val _navigateToSession = MutableLiveData<Long>()
    val navigateToSession
        get() = _navigateToSession
    // do the same for the chosen exercise ID
    private val _chosenExerciseId = MutableLiveData<Long>()
    val chosenExerciseId
        get() = _chosenExerciseId

    init {

    }


    fun onSessionNavigated(){
        _navigateToSession.value = -1
    }


}

class ExercisePickerViewModelFactory(
    private val dataSource: GymRepository,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExercisePickerViewModel::class.java)) {
            return ExercisePickerViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}