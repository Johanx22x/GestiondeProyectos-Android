package com.example.gestindeproyectos.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gestindeproyectos.R
import com.example.gestindeproyectos.db.DB
import com.example.gestindeproyectos.model.ForumItem
import java.text.SimpleDateFormat

class ForumItemRepliesAdapter(private val forumItems: List<ForumItem>) : RecyclerView.Adapter<ForumItemRepliesAdapter.ForumViewHolder>() {

    class ForumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val forumItemAuthor: TextView = itemView.findViewById(R.id.forum_item_author)
        val forumItemDate: TextView = itemView.findViewById(R.id.forum_item_date)
        val forumItemContent: TextView = itemView.findViewById(R.id.forum_item_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.forum_item_reply_card, parent, false)
        return ForumViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        val currentItem = forumItems[position]
        DB.instance.fetchCollaborator(currentItem.getAuthorId()).thenAccept { collaborator ->
            holder.forumItemAuthor.text = collaborator?.getFullName()
        }
        holder.forumItemDate.text = SimpleDateFormat("dd/MM/yyyy - HH:mm").format(currentItem.getTimestamp()!!.toDate())
        holder.forumItemContent.text = currentItem.getContent()
    }

    override fun getItemCount() = forumItems.size
}