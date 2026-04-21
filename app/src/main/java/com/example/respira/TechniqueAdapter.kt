package com.example.respira

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.respira.databinding.ItemTechniqueBinding

class TechniqueAdapter : ListAdapter<BreathingTechnique, TechniqueAdapter.TechniqueViewHolder>(TechniqueDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechniqueViewHolder {
        val binding = ItemTechniqueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TechniqueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TechniqueViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TechniqueViewHolder(private val binding: ItemTechniqueBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(technique: BreathingTechnique) {
            binding.tvItemTitle.text = technique.title
            binding.tvItemPattern.text = "In: ${technique.inhale}s | Hold: ${technique.hold}s | Out: ${technique.exhale}s"
        }
    }

    class TechniqueDiffCallback : DiffUtil.ItemCallback<BreathingTechnique>() {
        override fun areItemsTheSame(oldItem: BreathingTechnique, newItem: BreathingTechnique): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BreathingTechnique, newItem: BreathingTechnique): Boolean {
            return oldItem == newItem
        }
    }
}