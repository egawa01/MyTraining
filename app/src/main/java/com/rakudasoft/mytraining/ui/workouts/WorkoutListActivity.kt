package com.rakudasoft.mytraining.ui.workouts

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rakudasoft.mytraining.R
import com.rakudasoft.mytraining.databinding.ActivityWorkoutListBinding

class WorkoutListActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityWorkoutListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        /// 表示するテキスト配列を作る [テキスト0, テキスト1, ....]
        val list = List<String>(10) {"テキスト$it"}

        for (item in list) {
            Log.d("VERBOSE", "list item $item")
        }
        val adapter = WorkoutListViewAdapter(this, list)
        val layoutManager = LinearLayoutManager(this)

        // アダプターとレイアウトマネージャーをセット
        val recyclerView : RecyclerView = findViewById(R.id.workoutListRecyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        //recyclerView.setHasFixedSize(true)

    }

}