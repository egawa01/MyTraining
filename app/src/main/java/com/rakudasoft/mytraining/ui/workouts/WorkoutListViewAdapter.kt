package com.rakudasoft.mytraining.ui.workouts

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.rakudasoft.mytraining.R
import com.rakudasoft.mytraining.data.model.Workout

class WorkoutListViewAdapter(private val context : Context, private val list:List<Workout>)
    : RecyclerView.Adapter<WorkoutViewHolder>() {

    private val logLevel = "VERBOSE"

    private val layoutInflater = LayoutInflater.from(context)

    private val userId : String = (context as WorkoutListActivity).userId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        Log.d("VERBOSE", "onCreateViewHolder")
        val view = layoutInflater.inflate(R.layout.workout_item, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun getItemCount(): Int {
        val size = list.size
        Log.d("VERBOSE", "getItemCount $size")
        return size
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        Log.d("VERBOSE", "onBindViewHolder $position")
        holder.setWorkout(list[position])
        holder.itemView.setOnClickListener {
            Log.d(logLevel, "holder.itemView.onClickListener process")

            val workoutId: String? = holder.getWorkout()?.id
            Log.d(logLevel, "userId is $userId")
            Log.d(logLevel, "workoutId is $workoutId")

            if (workoutId != null) {
                Log.d(logLevel, "starting WorkoutEditActivity...")
                val intent = Intent(it.context, WorkoutEditActivity::class.java)
                intent.putExtra(WorkoutEditActivity.EXTRA_USER_ID, userId)
                intent.putExtra(WorkoutEditActivity.EXTRA_WORKOUT_ID, workoutId)
                (context as WorkoutListActivity).startActivityForResult(
                    intent,
                    WorkoutEditActivity.REQUEST_CODE
                )
            } else {
                Log.d(logLevel, "workoutId is null")
            }
        }
    }
}