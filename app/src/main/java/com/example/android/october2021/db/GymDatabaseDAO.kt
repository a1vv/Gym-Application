package com.example.android.october2021.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.october2021.db.entities.*

@Dao
interface GymDatabaseDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSession(session: Session)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercise(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSessionExercise(item: SessionExercise)

    @Update
    suspend fun updateSession(session: Session)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Query("SELECT * from sessions WHERE sessionId = :key")
    suspend fun getSession(key: Long): Session?

    @Query("SELECT * from exercises WHERE exerciseId = :key")
    suspend fun getExercise(key: Long): Exercise?

    @Query("DELETE FROM sessions")
    suspend fun clearSessions()

    @Query("SELECT * FROM sessions ORDER BY sessionId DESC")
    fun getAllSessions(): LiveData<List<Session>>

    @Query("SELECT * FROM exercises ORDER BY exerciseId DESC")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM sessions ORDER BY start_time_milli DESC LIMIT 1")
    suspend fun getLastSession(): Session?

    @Query("SELECT * FROM exercises ORDER BY exerciseId DESC LIMIT 1")
    suspend fun getLastExercise(): Exercise?

    @Query("SELECT * from sessions WHERE sessionId = :key")
    fun getSessionWithId(key: Long): Session?

    @Query("SELECT * from exercises WHERE exerciseId = :key")
    fun getExerciseWithId(key: Long): Exercise?

    @Transaction
    @Query("SELECT * FROM sessions WHERE sessionId = :key")
    fun getSessionWithSessionExercises(key: Long) : SessionWithSessionExercises

    @Transaction
    @Query("SELECT * FROM exercises")
    fun getExerciseAndSessionExercises(): List<ExerciseWithSessionExercises>

}

