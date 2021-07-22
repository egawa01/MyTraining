package com.rakudasoft.mytraining.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.rakudasoft.mytraining.data.model.Workout
import java.lang.Exception

class WorkoutDataSource (val userId : String) {
    private var store : FirebaseFirestore = FirebaseFirestore.getInstance()

    var createSuccessListener   : OnSuccessListener<Workout>? = null
    var createFailureListener   : OnFailureListener? = null
    var getSuccessListener       : OnSuccessListener<List<Workout>>? = null
    var getFailureListener       : OnFailureListener? = null

    private val logLevel = "VERBOSE"
    private val collectionPath = "users/$userId/workouts"

    fun create(workout : Workout) {
        Log.d(logLevel, "WorkoutDataSource.create() start")
        try {
            store.collection(collectionPath)
                .document(workout.id)
                .set(getDbData(workout))
                .addOnSuccessListener {
                    Log.d("VERBOSE", "WorkoutDataSource.create() success")
                    createSuccessListener?.invoke(workout)
                }.addOnFailureListener {
                    Log.d("VERBOSE", "WorkoutDataSource.create() failure $it")
                    createFailureListener?.invoke(it)
                }
        } catch (exception: Exception) {
            Log.d(logLevel, "WorkoutDataSource.create() error $exception")
            createFailureListener?.invoke(exception)

        }
    }

    fun get() {
        Log.d(logLevel, "WorkoutDataSource.get() start")
        try {
            store.collection(collectionPath)
                .get()
                .addOnSuccessListener {
                    Log.d("VERBOSE", "WorkoutDataSource.get() success")
                    val list = mutableListOf<Workout>()
                    for (document in it) {
                        Log.d(logLevel, document.id)
                        list.add(getWorkout(document))
                    }
                    getSuccessListener?.invoke(list)
                }
                .addOnFailureListener {
                    Log.d(logLevel, "WorkoutDataSource.get() Error $it")
                    getFailureListener?.invoke(it)
                }
        }
        catch (e : Exception) {
            Log.d(logLevel, "WorkoutDataSource.get() Error $e")
            getFailureListener?.invoke(e)
        }
    }

    private fun getDbData(workout: Workout): Map<String, Any> {
        return mutableMapOf(
            "id" to workout.id,
            "name" to workout.name,
            "description" to workout.description
        )
    }

    private fun getWorkout(document: QueryDocumentSnapshot): Workout {
        return Workout(
            id = document["id"] as String,
            name = document["name"] as String,
            description = document["description"] as String
        )
    }
}