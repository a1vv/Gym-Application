package com.example.android.october2021.sessions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.android.october2021.R
import com.example.android.october2021.databinding.FragmentSessionInfoBinding
import com.example.android.october2021.db.GymDatabase
import com.example.android.october2021.db.GymRepository

class SessionInfo  : Fragment(R.layout.fragment_session_info) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_session_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSessionInfoBinding.bind(view)
        val application = requireNotNull(this.activity).application

        val sessionId = requireArguments().getLong("sessionId")

        // instantiate sessionViewModel via viewModelFactory, pass it to xml via binding.
        val viewModelFactory = SessionViewModel.SessionViewModelFactory(
            GymRepository(GymDatabase.getInstance(application)), application, sessionId
        )
        val viewModel : SessionViewModel by navGraphViewModels(R.id.session_navigation){viewModelFactory}


        binding.sessionViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner



    }
}