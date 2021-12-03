package com.example.android.october2021.sessionexercises

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.october2021.databinding.SessionExerciseItemBinding
import com.example.android.october2021.db.entities.SessionExerciseWithExercise

class SessionExerciseAdapter(private val clickListener: SessionExerciseWithExerciseListener) :
    ListAdapter<SessionExerciseWithExercise, SessionExerciseAdapter.SessionExerciseWithExerciseHolder>(
        SessionExerciseWithExerciseDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SessionExerciseWithExerciseHolder {
        return SessionExerciseWithExerciseHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SessionExerciseWithExerciseHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!, clickListener)
    }

    class SessionExerciseWithExerciseHolder(private val binding: SessionExerciseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: SessionExerciseWithExercise,
            clickListener: SessionExerciseWithExerciseListener
        ) {
            binding.sessionExerciseWithExercise = item
            binding.sessionExercise = item.sessionExercise
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): SessionExerciseWithExerciseHolder {
                val binding = SessionExerciseItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return SessionExerciseWithExerciseHolder(binding)
            }
        }
    }

    class SessionExerciseWithExerciseDiffCallback :
        DiffUtil.ItemCallback<SessionExerciseWithExercise>() {
        override fun areItemsTheSame(
            oldItem: SessionExerciseWithExercise,
            newItem: SessionExerciseWithExercise
        ): Boolean {
            return oldItem.sessionExercise.sessionExerciseId == newItem.sessionExercise.sessionExerciseId
        }

        override fun areContentsTheSame(
            oldItem: SessionExerciseWithExercise,
            newItem: SessionExerciseWithExercise
        ): Boolean {
            return false // TODO: compare contents of old and new item
        }
    }

}

// returns the id of the clicked SessionExerciseWithExercise
class SessionExerciseWithExerciseListener(val clickListener: (sessionExerciseId: Long) -> Unit) {
    fun onClick(sessionExerciseWithExercise: SessionExerciseWithExercise) =
        clickListener(sessionExerciseWithExercise.sessionExercise.sessionExerciseId)
}