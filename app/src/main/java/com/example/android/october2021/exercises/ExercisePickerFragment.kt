package com.example.android.october2021.exercises

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.october2021.R
import com.example.android.october2021.databinding.FragmentExercisePickerBinding
import com.example.android.october2021.db.GymDatabase
import com.example.android.october2021.db.GymRepository

class ExercisePickerFragment : Fragment(R.layout.fragment_exercise_picker) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentExercisePickerBinding.bind(view)
        val application = requireNotNull(this.activity).application
        val dataSource = GymRepository(GymDatabase.getInstance(application))
        val viewModelFactory = ExercisePickerViewModelFactory(dataSource, application)
        val viewModel : ExercisePickerViewModel by navGraphViewModels(R.id.session_navigation){viewModelFactory}


        // get arguments from bundle
        val sessionId = ExercisePickerFragmentArgs.fromBundle(requireArguments()).sessionId
        Log.d("EPF","sID: $sessionId received from SF")

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        // set list to scroll from bottom to top
        layoutManager.reverseLayout = true

        val adapter = ExercisesAdapter(ExerciseListener { exerciseId ->
            Log.d("EF", "exercise item clicked")
            this.findNavController()
                .navigate(ExercisePickerFragmentDirections.actionExercisePickerFragmentToSessionFragment(sessionId,exerciseId))
            viewModel.onSessionNavigated()
        })
        binding.viewModel = viewModel
        binding.exercisesList.adapter = adapter
        binding.exercisesList.layoutManager = layoutManager

        viewModel.exercises.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })
    }
}