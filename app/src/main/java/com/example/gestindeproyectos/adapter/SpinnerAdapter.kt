package com.example.gestindeproyectos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestindeproyectos.R
import com.example.gestindeproyectos.model.Task

class SpinnerAdapter(private val taskList: List<Task>, private val spinner: Spinner) : RecyclerView.Adapter<SpinnerAdapter.SpinnerViewHolder>() {

    class SpinnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       // val taskName: TextView = itemView.findViewById(R.id.spinner1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinnerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_add_task, parent, false)
        return SpinnerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SpinnerViewHolder, position: Int) {
        val currentItem = taskList[position]
       // holder.taskName.text = currentItem.getDescription()
    }

    override fun getItemCount() = taskList.size

    fun setSpinner() {
        val adapter = ArrayAdapter(spinner.context, android.R.layout.simple_spinner_item, taskList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}