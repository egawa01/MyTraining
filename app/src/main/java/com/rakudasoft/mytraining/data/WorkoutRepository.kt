package com.rakudasoft.mytraining.data

import android.util.Log
import com.rakudasoft.mytraining.data.model.Workout

class WorkoutRepository(private val dataSource: WorkoutDataSource) {
    var getSuccessListener      : OnSuccessListener<List<Workout>>? = null
    var getFailureListener       : OnFailureListener? = null
    var createSuccessListener   : OnSuccessListener<Workout>? = null
    var createFailureListener   : OnFailureListener? = null
    var getItemSuccessListener      : OnSuccessListener<Workout>? = null
    var getItemFailureListener       : OnFailureListener? = null
    var updateSuccessListener   : OnSuccessListener<Workout>? = null
    var updateFailureListener   : OnFailureListener? = null

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

        dataSource.getItemSuccessListener = {
            getItemSuccessListener?.invoke(it)
        }

        dataSource.getItemFailureListener = {
            getItemFailureListener?.invoke(it)
        }

        dataSource.updateSuccessListener = {
            updateSuccessListener?.invoke(it)
        }

        dataSource.updateFailureListener = {
            updateFailureListener?.invoke(it)
        }
    }

    fun getAll() {
        Log.d(logLevel, "WorkoutRepository.get() start")
        dataSource.getAll()
    }

    fun create(workOut : Workout) {
        Log.d(logLevel, "WorkoutRepository.create() start")
        dataSource.create(workOut)
    }

    fun getitem(workoutId: String) {
        Log.d(logLevel, "WorkoutRepository.getItem() start")
        dataSource.getItem(workoutId)
    }

    fun update(workOut : Workout) {
        Log.d(logLevel, "WorkoutRepository.update() start")
        dataSource.update(workOut)
    }
}