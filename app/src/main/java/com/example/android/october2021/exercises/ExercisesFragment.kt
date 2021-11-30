package com.example.android.october2021.exercises

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.october2021.R
import com.example.android.october2021.databinding.FragmentExercisesBinding
import com.example.android.october2021.db.GymDatabase

class ExercisesFragment : Fragment(R.layout.fragment_exercises) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // instantiate binding and viewmodel
        val binding = FragmentExercisesBinding.bind(view)

        val application = requireNotNull(this.activity).application
        val dataSource = GymDatabase.getInstance(application).gymDatabaseDAO
        val viewModelFactory = WorkoutsViewModelFactory(dataSource, application)
        val exercisesViewModel = ViewModelProvider(
            this, viewModelFactory).get(ExercisesViewModel::class.java)
        binding.exercisesViewModel = exercisesViewModel
        binding.lifecycleOwner = this

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        val adapter = ExercisesAdapter()
        binding.exercisesList.adapter = adapter
        binding.exercisesList.layoutManager = layoutManager

        exercisesViewModel.exercises.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                Log.d("WF","observed change in workouts")
            }
        })
    }
}