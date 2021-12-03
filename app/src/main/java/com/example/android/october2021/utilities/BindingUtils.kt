package com.example.android.october2021.utilities

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.october2021.db.entities.Session
import com.example.android.october2021.db.entities.Exercise
import com.example.android.october2021.db.entities.SessionExercise
import com.example.android.october2021.db.entities.SessionExerciseWithExercise

@BindingAdapter("exerciseTitle")
fun TextView.setExerciseTitle(item: Exercise){
    text = item.exerciseTitle
}


@BindingAdapter("exerciseId")
fun TextView.setExerciseId(item: Exercise){
    text = item.exerciseId.toString()
}

@BindingAdapter("sessionInfo")
fun TextView.setSessionInfo(item: Session?){
    if (item != null) {
        val time = (item.endTimeMilli - item.startTimeMilli)/1000
        "[${item.sessionId}] ${time/60} minutes and $time seconds. ".also { text = it }
    }
}

@BindingAdapter("sessionExerciseTest")
fun TextView.setSessionExerciseTest(item: SessionExerciseWithExercise?){
    if (item != null) {
        if(item.exercise != null) {
            text = "JA?"
        } else {
            text = "nej."
        }
    }
}

@BindingAdapter("sessionExerciseInfo")
fun TextView.setSessionExerciseInfo(item: SessionExerciseWithExercise?){
    if (item != null) {
        text = item.sessionExercise.sessionExerciseText
    }
}


