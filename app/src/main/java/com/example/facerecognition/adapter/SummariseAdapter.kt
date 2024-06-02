package com.example.facerecognition.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.facerecognition.databinding.ItemSummariseBinding
import com.example.facerecognition.databinding.ItemSummariseDateBinding

sealed class SummariseItem {
    data class DateItem(val date: String) : SummariseItem()
    data class AttendeeItem(val matric: String, val name: String, val time: String) : SummariseItem()
}

class SummariseAdapter(private val items: List<SummariseItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_DATE = 0
    private val TYPE_ATTENDEE = 1

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is SummariseItem.DateItem -> TYPE_DATE
            is SummariseItem.AttendeeItem -> TYPE_ATTENDEE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DATE -> {
                val binding = ItemSummariseDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DateViewHolder(binding)
            }
            TYPE_ATTENDEE -> {
                val binding = ItemSummariseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AttendeeViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is SummariseItem.DateItem -> (holder as DateViewHolder).bind(item.date)
            is SummariseItem.AttendeeItem -> (holder as AttendeeViewHolder).bind(item.matric, item.name, item.time)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class DateViewHolder(private val binding: ItemSummariseDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(date: String) {
            binding.dateText.text = date
        }
    }

    inner class AttendeeViewHolder(private val binding: ItemSummariseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(matric: String, name: String, time: String) {
            binding.matricText.text = matric
            binding.nameText.text = name
            binding.timeText.text = time
        }
    }
}
