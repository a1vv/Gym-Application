package com.example.android.october2021.sessions

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.october2021.R
import com.example.android.october2021.db.GymDatabase
import com.example.android.october2021.databinding.FragmentSessionBinding
import com.example.android.october2021.db.GymRepository
import com.example.android.october2021.sessionexercises.SessionExerciseAdapter
import com.example.android.october2021.sessionexercises.SessionExerciseWithExerciseListener

class SessionFragment : Fragment(R.layout.fragment_session) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSessionBinding.bind(view)
        val application = requireNotNull(this.activity).application

        // get sessionId from arguments
        val sessionId = SessionFragmentArgs.fromBundle(requireArguments()).sessionId

        // instantiate sessionViewModel via viewModelFactory, pass it to xml via binding.
        val viewModelFactory = SessionViewModel.SessionViewModelFactory(GymRepository(GymDatabase.getInstance(application)),application,sessionId)
        val sessionViewModel = ViewModelProvider(
            this, viewModelFactory).get(SessionViewModel::class.java)
        binding.sessionViewModel = sessionViewModel
        binding.lifecycleOwner = this

        sessionViewModel.navigateToHome.observe(viewLifecycleOwner,{
            val extras = FragmentNavigatorExtras(binding.fab to "fab")
            it?.let{
                this.findNavController().navigate(SessionFragmentDirections.actionSessionFragmentToHomeFragment(),extras)
                sessionViewModel.onHomeNavigated()
            }
        })

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL


        val adapter = SessionExerciseAdapter(SessionExerciseWithExerciseListener { sessionExerciseId ->
            sessionViewModel.onSessionExerciseClicked(sessionExerciseId)
        })

        binding.exercisesList.adapter = adapter
        binding.exercisesList.layoutManager = layoutManager

        sessionViewModel.sessionExerciseList.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

    }
}