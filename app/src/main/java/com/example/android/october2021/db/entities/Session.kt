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
 * Every exercise can have multiple parent SessionExercises (one-many relation)
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
        entity = SessionExercise::class,
        entityColumn = "sessionExerciseId",
        associateBy = Junction(SessionExerciseSessionCrossRef::class)
    )
    val sessionExercises: List<SessionExerciseWithExercise>
)

data class SessionExerciseWithExercise(
    @Embedded val sessionExercise: SessionExercise,
    @Relation(
        parentColumn = "sessionExerciseId",
        entity = Exercise::class,
        entityColumn = "exerciseId",
        associateBy = Junction(SessionExerciseExerciseCrossRef::class)
    )
    val exercise: Exercise

)

@Entity(primaryKeys = ["sessionExerciseId", "exerciseId"])
data class SessionExerciseExerciseCrossRef(
    val sessionExerciseId: Long,
    @ColumnInfo(index = true) val exerciseId: Long
)

@Entity(primaryKeys = ["sessionExerciseId", "sessionId"])
data class SessionExerciseSessionCrossRef(
    val sessionExerciseId: Long,
    @ColumnInfo(index = true) val sessionId: Long
)

/**
 * SessionExercise is an exercise in a session. The exercise it's connected to is embedded
 */
@Entity(tableName = "sessionExercises")
data class SessionExercise(
    @PrimaryKey(autoGenerate = true) var sessionExerciseId: Long = 0,

    val sessionExerciseText: String = "cock",

    @ColumnInfo(index = true) val parentSessionId: Long,
    @ColumnInfo(index = true) val parentExerciseId: Long

)

