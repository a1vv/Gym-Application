package com.example.android.october2021.db

import com.example.android.october2021.db.entities.Session
import com.example.android.october2021.db.entities.SessionExercise

class GymRepository(private val db: GymDatabase) {



    fun getSessionWithSessionExercises(sessionId: Long) =
        db.gymDatabaseDAO.getSessionWithExercises(sessionId)


    fun getExerciseWithSessionExercises(workoutId: Long) =
        db.gymDatabaseDAO.getExerciseWithExercises(workoutId)

     fun getExerciseWithSessionExercises() =
            db.gymDatabaseDAO.getExerciseAndSessionExercises()

    suspend fun updateSession(item: Session) =
        db.gymDatabaseDAO.updateSession(item)

    fun getSessionWithId(id: Long) =
        db.gymDatabaseDAO.getSessionWithId(id)

    fun getAllExercises() =
        db.gymDatabaseDAO.getAllExercises()

    suspend fun insertSession(item: Session) =
        db.gymDatabaseDAO.insertSession(item)

    suspend fun getLastSession() =
        db.gymDatabaseDAO.getLastSession()

    suspend fun getLastExercise() =
        db.gymDatabaseDAO.getLastExercise()

    suspend fun insertSessionExercise(sessionExercise: SessionExercise) =
        db.gymDatabaseDAO.insertSessionExercise(sessionExercise)

}
