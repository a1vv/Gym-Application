package com.example.android.october2021.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.october2021.db.entities.Exercise
import com.example.android.october2021.databinding.ExerciseItemBinding

class ExercisesAdapter: ListAdapter<Exercise, ExercisesAdapter.ExerciseHolder>(ExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        return ExerciseHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ExerciseHolder(private val binding: ExerciseItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Exercise){
            binding.exercise = item
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ExerciseHolder {
                val binding = ExerciseItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return ExerciseHolder(binding)
            }
        }

    }

    class ExerciseDiffCallback : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.exerciseId == newItem.exerciseId
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.exerciseTitle == newItem.exerciseTitle
        }
    }

}