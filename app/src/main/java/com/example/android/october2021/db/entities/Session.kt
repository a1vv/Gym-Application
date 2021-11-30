package com.example.android.october2021.db.entities

import androidx.room.*

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

data class ExerciseWithSessionExercises(
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "parentExerciseId"
    )
    val sessionExercises: List<SessionExercise>
)

data class SessionWithSessionExercises(
    @Embedded val session: Session,
    @Relation(
        parentColumn = "sessionId",
        entityColumn = "parentSessionId"
    )
    val sessionExercises: List<SessionExercise>
)

// SessionExercise is an exercise in a session, holds a reference to it's session, and gets
// exercise-specific information by holding an exerciseId

@Entity(tableName = "sessionExercises")
data class SessionExercise(
    @PrimaryKey(autoGenerate = true) var sessionExerciseId: Long = 0,

    val sessionExerciseText: String = "cock",
    val parentSessionId: Long,
    val parentExerciseId: Long

    )