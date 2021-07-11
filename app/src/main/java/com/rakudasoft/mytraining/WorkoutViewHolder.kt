package com.rakudasoft.mytraining

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rakudasoft.mytraining.databinding.ActivityWorkoutListBinding

class WorkoutViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    val workoutName : TextView = itemView.findViewById(R.id.workoutName)
}