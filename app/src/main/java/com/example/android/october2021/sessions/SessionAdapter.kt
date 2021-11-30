package com.example.android.october2021.sessions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.october2021.db.entities.Session
import com.example.android.october2021.databinding.SessionItemBinding

class SessionAdapter(val clickListener: SessionListener): ListAdapter<Session, SessionAdapter.SessionHolder>(
    SessionDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionHolder {
        return SessionHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SessionHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!,clickListener)
    }

    class SessionHolder(private val binding: SessionItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Session, clickListener: SessionListener){
            binding.session = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }



        companion object{
            fun from(parent: ViewGroup): SessionHolder {
                val binding = SessionItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return SessionHolder(binding)
            }
        }

    }

    class SessionDiffCallback : DiffUtil.ItemCallback<Session>() {
        override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
            return oldItem.sessionId == newItem.sessionId
        }

        override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
            return oldItem.startTimeMilli == newItem.startTimeMilli && oldItem.endTimeMilli == newItem.endTimeMilli
        }
    }

}

class SessionListener(val clickListener: (sessionId: Long) -> Unit){
    fun onClick(session: Session) = clickListener(session.sessionId)
}