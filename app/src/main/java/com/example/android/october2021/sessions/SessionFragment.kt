package com.example.android.october2021.sessions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.android.october2021.R
import com.example.android.october2021.databinding.FragmentSessionImprovedBinding
import com.example.android.october2021.db.GymDatabase
import com.example.android.october2021.db.GymRepository
import com.example.android.october2021.sessionexercises.SessionExerciseAdapter
import com.example.android.october2021.sessionexercises.SessionExerciseWithExerciseListener

class SessionFragment : Fragment(R.layout.fragment_session_improved) {

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session_improved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSessionImprovedBinding.bind(view)
        val application = requireNotNull(this.activity).application

        // get sessionId from arguments
        val sessionId = SessionFragmentArgs.fromBundle(requireArguments()).sessionId

        // instantiate sessionViewModel via viewModelFactory, pass it to xml via binding.
        val viewModelFactory = SessionViewModel.SessionViewModelFactory(
            GymRepository(GymDatabase.getInstance(application)), application, sessionId
        )
        val sessionViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(SessionViewModel::class.java)

        binding.sessionViewModel = sessionViewModel
        binding.lifecycleOwner = this


        // add navigation back to home TODO: remove?
        sessionViewModel.navigateToHome.observe(viewLifecycleOwner, {
            val extras = FragmentNavigatorExtras(binding.fab to "fab")
            it?.let {
                this.findNavController().navigate(
                    SessionFragmentDirections.actionSessionFragmentToHomeFragment(),
                    extras
                )
                sessionViewModel.onHomeNavigated()
            }
        })

        // create layoutManager for sessionExercises
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        // create adapter for sessionExercises
        val adapter =
            SessionExerciseAdapter(SessionExerciseWithExerciseListener { sessionExerciseId ->
                sessionViewModel.onSessionExerciseClicked(sessionExerciseId)
            })
        // bind adapter and layoutManager to exercisesList
        binding.exercisesList.adapter = adapter
        binding.exercisesList.layoutManager = layoutManager

        // submit a new list to viewModel every time it gets updated
        sessionViewModel.sessionExerciseList.observe(viewLifecycleOwner, {
            Log.d("SF", "SessionExerciseList updated")
            it?.let {
                adapter.submitList(it)
            }
        })


        // list of fragments for viewpager
        val fragmentList = arrayListOf(
            SessionInfo(),
            SessionSettings()
        )

        // implement viewpager to scroll between different session-information
        viewPager = binding.viewPager
        viewPager.adapter =
            ScreenSlidePagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)

    }

    // adapter that handles the different fragments to display in viewpager
    private inner class ScreenSlidePagerAdapter(
        list: ArrayList<Fragment>,
        fm: FragmentManager,
        lifecycle: Lifecycle
    ) : FragmentStateAdapter(fm, lifecycle) {

        private val fragmentList = list

        override fun getItemCount(): Int = fragmentList.size

        override fun createFragment(position: Int): Fragment = fragmentList[position]
    }
}