package com.example.android.october2021.utilities

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.example.android.october2021.db.entities.Exercise
import com.example.android.october2021.db.entities.Session
import com.example.android.october2021.db.entities.SessionExerciseWithExercise


@BindingAdapter("exerciseTitle")
fun TextView.setExerciseTitle(item: Exercise){
    text = item.exerciseTitle
}


@BindingAdapter("exerciseId")
fun TextView.setExerciseId(item: Exercise){
    text = item.exerciseId.toString()
}

@BindingConversion
fun longToStr(value: Long?): String {
    return if(value?.toString()!=null){
        value.toString()
    } else {
        ""
    }
}

@BindingAdapter("android:text")
fun TextView.setText(value: Long?){
    text = longToStr(value)
}

// TODO: time should be start time and end time, not the difference.
// TODO: add on click listener in fragment_session_info.xml to update end time
// TODO: every editable session attribute should be visually distinct (perhaps as an outlined button)
// TODO: pressing the time should open a modal interface that let's you pick start and end time - like Sleep As Android (date, start time, end time)
@BindingAdapter("session_time")
fun TextView.setSessionTime(session: Session?){
    if(session != null){
        val time = (session.endTimeMilli - session.startTimeMilli)/1000
        "${time/60} minutes and $time seconds.".also { text = it }
    }

}

@BindingAdapter("sessionExerciseTest")
fun TextView.setSessionExerciseTest(item: SessionExerciseWithExercise?){
    if (item != null) {
        text = item.exercise.exerciseTitle
    }
}

@BindingAdapter("sessionExerciseInfo")
fun TextView.setSessionExerciseInfo(item: SessionExerciseWithExercise?){
    if (item != null) {
        text = item.sessionExercise.sessionExerciseId.toString()
    }
}


