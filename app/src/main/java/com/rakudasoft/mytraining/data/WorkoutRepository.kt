package com.rakudasoft.mytraining.data

import android.util.Log
import com.rakudasoft.mytraining.data.model.Workout

class WorkoutRepository(private val dataSource: WorkoutDataSource) {
    var getSuccessListener      : OnSuccessListener<List<Workout>>? = null
    var getFailureListener       : OnFailureListener? = null
    var createSuccessListener   : OnSuccessListener<Workout>? = null
    var createFailureListener   : OnFailureListener? = null

    private val logLevel = "VERBOSE"

    init {
        Log.d(logLevel, "WorkoutRepository.init() start")

        dataSource.getSuccessListener = {
            getSuccessListener?.invoke(it)
        }

        dataSource.getFailureListener = {
            getFailureListener?.invoke(it)
        }

        dataSource.createSuccessListener = {
            createSuccessListener?.invoke(it)
        }

        dataSource.createFailureListener = {
            createFailureListener?.invoke(it)
        }
    }

    fun get() {
        Log.d(logLevel, "WorkoutRepository.get() start")
        dataSource.get()
    }

    fun create(workOut : Workout) {
        Log.d(logLevel, "WorkoutRepository.create() start")
        dataSource.create(workOut)
    }

}