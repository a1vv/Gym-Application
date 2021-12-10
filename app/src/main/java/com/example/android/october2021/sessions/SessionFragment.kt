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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.android.october2021.R
import com.example.android.october2021.databinding.FragmentSessionBinding
import com.example.android.october2021.db.GymDatabase
import com.example.android.october2021.db.GymRepository
import com.example.android.october2021.sessionexercises.SessionExerciseWithExerciseAdapter
import com.example.android.october2021.sessionexercises.SessionExerciseWithExerciseListener

class SessionFragment : Fragment(R.layout.fragment_session) {

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSessionBinding.bind(view)
        val application = requireNotNull(this.activity).application

        // get sessionId from arguments
        val argSessionId = SessionFragmentArgs.fromBundle(requireArguments()).sessionId
        val argExerciseId = SessionFragmentArgs.fromBundle(requireArguments()).exerciseId

        // instantiate sessionViewModel via viewModelFactory, pass it to xml via binding.
        val viewModelFactory = SessionViewModel.SessionViewModelFactory(
            GymRepository(GymDatabase.getInstance(application)), application, argSessionId
        )
        val viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(SessionViewModel::class.java)

        binding.sessionViewModel = viewModel
        binding.lifecycleOwner = this

        // send exerciseId to viewModel
        viewModel.onExerciseAdded(argExerciseId)


        // create layoutManager for sessionExercises
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        // create adapter for sessionExercises
        val adapter =
            SessionExerciseWithExerciseAdapter(SessionExerciseWithExerciseListener { sessionExerciseId ->
                viewModel.onSessionExerciseClicked(
                    sessionExerciseId
                )
            })

        // bind adapter and layoutManager to exercisesList
        binding.exercisesList.adapter = adapter
        binding.exercisesList.layoutManager = layoutManager

        // submit a new list to viewModel every time it gets updated
        viewModel.sessionExerciseList.observe(viewLifecycleOwner, {
            Log.d("SF", "SessionExerciseList updated")
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToExercisePicker.observe(viewLifecycleOwner, {
            it?.let { sessionId->
                this.findNavController().navigate(
                    SessionFragmentDirections.actionSessionFragmentToExercisePickerFragment(sessionId))
                viewModel.onExercisePickerNavigated()
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
            ScreenSlidePagerAdapter(
                fragmentList,
                requireActivity().supportFragmentManager,
                lifecycle
            )

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