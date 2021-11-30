package com.example.android.october2021.db.entities

import androidx.room.*

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
 * Every exercise can be connected to multiple SessionExercises (one-many relation)
 * This data class exists to get a list of all SessionExercises connected to an exercise
 */
data class ExerciseWithSessionExercises(
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "parentExerciseId"
    )
    val sessionExercises: List<SessionExercise>
)

/**
 * Every Session has multiple SessionExercises connected to it (one-many relation)
 * This data class allows you to get a list of all SessionExercises belonging to a Session.
 */
data class SessionWithSessionExercises(
    @Embedded val session: Session,
    @Relation(
        parentColumn = "sessionId",
        entityColumn = "parentSessionId"
    )
    val sessionExercises: List<SessionExercise>
)

/**
 * SessionExercise is an exercise in a session. The exercise it's connected to is embedded
 */
@Entity(tableName = "sessionExercises")
data class SessionExercise(
    @PrimaryKey(autoGenerate = true) var sessionExerciseId: Long = 0,

    val sessionExerciseText: String = "cock",
    val parentSessionId: Long,
    val parentExerciseId: Long,
    @Embedded val exercise: Exercise

    )