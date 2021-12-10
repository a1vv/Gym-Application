package com.example.android.october2021.db.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A workout-session contains multiple SessionExercises. Has a start and end-time.
 */
@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true)
    var sessionId: Long = 0L,

    @ColumnInfo(name = "start_time_milli")
    val startTimeMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "end_time_milli")
    var endTimeMilli: Long = startTimeMilli,

    @ColumnInfo(name = "training_type")
    var trainingType: String = "",

    )

/**
 * SessionExercise is an exercise in a session, foreign keys are the ID:s of Exercise and Session.
 * Many to one relation since every Session/Exercise can reference multiple SessionExercises
 */

@Entity(
    tableName = "sessionExercises",
//    foreignKeys = [
//        ForeignKey(
//            entity = Exercise::class,
//            parentColumns = arrayOf("exerciseId"),
//            childColumns = arrayOf("parentExerciseId")
//        ),
//        ForeignKey(
//            entity = Session::class,
//            parentColumns = arrayOf("sessionId"),
//            childColumns = arrayOf("parentSessionId")
//        )]
)
data class SessionExercise(
    @PrimaryKey(autoGenerate = true) var sessionExerciseId: Long = 0,

    val sessionExerciseText: String = "cock",

    @ColumnInfo(index = true) val parentSessionId: Long,
    @ColumnInfo(index = true)
    val parentExerciseId: Long
)

/**
 * Holds a sessionExercise and it's associated exercise. Embedded = bad? it works though.
 */
data class SessionExerciseWithExercise(
    @Embedded
    val sessionExercise: SessionExercise,
    @Embedded
    val exercise: Exercise
)


