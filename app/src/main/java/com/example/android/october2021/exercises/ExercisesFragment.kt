package com.example.android.october2021.exercises

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.october2021.R
import com.example.android.october2021.databinding.FragmentExercisesBinding
import com.example.android.october2021.db.GymDatabase
import com.example.android.october2021.db.GymRepository

class ExercisesFragment : Fragment(R.layout.fragment_exercises) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // instantiate binding and viewmodel
        val binding = FragmentExercisesBinding.bind(view)

        val application = requireNotNull(this.activity).application
        val dataSource = GymRepository(GymDatabase.getInstance(application))
        val viewModelFactory = ExercisesViewModelFactory(dataSource, application)
        val exercisesViewModel = ViewModelProvider(
            this, viewModelFactory).get(ExercisesViewModel::class.java)
        binding.exercisesViewModel = exercisesViewModel
        binding.lifecycleOwner = this

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        val adapter = ExercisesAdapter(ExerciseListener { Log.d("EF", "exercise item clicked") })
        binding.exercisesList.adapter = adapter
        binding.exercisesList.layoutManager = layoutManager

        exercisesViewModel.exercises.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
                Log.d("WF","observed change in workouts")
            }
        })
    }
}