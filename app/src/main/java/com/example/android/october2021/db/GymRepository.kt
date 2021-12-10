package com.example.android.october2021.db

import com.example.android.october2021.db.entities.Exercise
import com.example.android.october2021.db.entities.Session
import com.example.android.october2021.db.entities.SessionExercise

class GymRepository(
    private val db: GymDatabase
) {

    fun getSession(id: Long) =
        db.gymDatabaseDAO.getSessionWithId(id)

    fun getExercise(id: Long) =
        db.gymDatabaseDAO.getExerciseWithId(id)

    fun getAllExercises() =
        db.gymDatabaseDAO.getAllExercises()

    fun getSessionExercises(sessionId: Long) =
        db.gymDatabaseDAO.getTheFuckersNow(sessionId)

    suspend fun insertExercise(exercise: Exercise) =
        db.gymDatabaseDAO.insertExercise(exercise)

    suspend fun insertSession(item: Session) : Long =
        db.gymDatabaseDAO.insertSession(item)

    suspend fun insertSessionExercise(sessionExercise: SessionExercise) =
        db.gymDatabaseDAO.insertSessionExercise(sessionExercise)

    suspend fun updateSession(item: Session) =
        db.gymDatabaseDAO.updateSession(item)

    suspend fun getLastSession() =
        db.gymDatabaseDAO.getLastSession()

    suspend fun getLastExercise() =
        db.gymDatabaseDAO.getLastExercise()

    fun clearAllTables() = db.clearAllTables()

}
