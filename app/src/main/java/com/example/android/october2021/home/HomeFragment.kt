package com.example.android.october2021.home

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.october2021.R
import com.example.android.october2021.databinding.FragmentHomeBinding
import com.example.android.october2021.db.GymDatabase
import com.example.android.october2021.sessions.SessionAdapter
import com.example.android.october2021.sessions.SessionListener

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)
        val application = requireNotNull(this.activity).application
        val sessionSource = GymDatabase.getInstance(application).gymDatabaseDAO
        val viewModelFactory = HomeViewModelFactory(sessionSource, application)
        val homeViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(HomeViewModel::class.java)

        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    true
                }
                R.id.workouts -> {
                    homeViewModel.onExercisesClicked()
                    true
                }
                R.id.clearSessions -> {
                    homeViewModel.onClearSessions()
                    true
                }
                else -> false
            }
        }

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        val adapter = SessionAdapter(SessionListener { sessionId ->
            homeViewModel.onSessionClicked(sessionId)
        })
        binding.sessionList.adapter = adapter
        binding.sessionList.layoutManager = layoutManager

        // observe sessions database and submit to adapter to keep recyclerview updated.
        homeViewModel.sessions.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
                Log.d("WF", "observed change in workouts")
            }
        })

        homeViewModel.navigateToSession.observe(viewLifecycleOwner, { session ->
            val extras = FragmentNavigatorExtras(binding.fab to "fab")
            session?.let {
                this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSessionFragment(session),
                    extras
                )
                homeViewModel.onSessionNavigated()
            }
        })

        homeViewModel.navigateToExercises.observe(viewLifecycleOwner, {
            Log.d("HF", "Observed change in navigateToWorkouts")
            if (it > 0) it.let {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToWorkoutsFragment())
                homeViewModel.onExercisesNavigated()
            }
        })
    }
}