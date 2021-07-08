package com.example.eperdemic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.event_item.view.*

class EventAdapter(
    private val events:MutableList<Event>
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>(){

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.event_item,
                parent,
                false
            )
        )
    }

    fun addEvent(event : Event){
        events.add(event)
        notifyItemInserted(events.size - 1)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val curEvent = events[position]
        holder.itemView.apply {
            tvEvento.text =  curEvent.text
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }
}