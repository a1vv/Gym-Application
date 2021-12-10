package com.example.android.october2021.sessionexercises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.october2021.databinding.SessionExerciseItemBinding
import com.example.android.october2021.db.entities.SessionExercise

class SessionExerciseAdapter(private val clickListener: SessionExerciseListener) :
    ListAdapter<SessionExercise, SessionExerciseAdapter.SessionExerciseHolder>(
        SessionExerciseDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SessionExerciseHolder {
        return SessionExerciseHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SessionExerciseHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!, clickListener)
    }

    class SessionExerciseHolder(private val binding: SessionExerciseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: SessionExercise,
            clickListener: SessionExerciseListener
        ) {
            binding.sessionExercise = item
            //binding.clickListener = clickListener
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): SessionExerciseHolder {
                val binding = SessionExerciseItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return SessionExerciseHolder(binding)
            }
        }
    }

    class SessionExerciseDiffCallback :
        DiffUtil.ItemCallback<SessionExercise>() {
        override fun areItemsTheSame(
            oldItem: SessionExercise,
            newItem: SessionExercise
        ): Boolean {
            return oldItem.sessionExerciseId == newItem.sessionExerciseId
        }

        override fun areContentsTheSame(
            oldItem: SessionExercise,
            newItem: SessionExercise
        ): Boolean {
            return false // TODO: compare contents of old and new item
        }
    }

}

// returns the id of the clicked SessionExerciseWithExercise
class SessionExerciseListener(val clickListener: (sessionExerciseId: Long) -> Unit) {
    fun onClick(sessionExercise: SessionExercise) =
        clickListener(sessionExercise.sessionExerciseId)
}