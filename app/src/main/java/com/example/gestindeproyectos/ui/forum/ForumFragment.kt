package com.example.gestindeproyectos.ui.forum

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestindeproyectos.adapter.ForumItemAdapter
import com.example.gestindeproyectos.databinding.FragmentForumBinding
import com.example.gestindeproyectos.db.DB
import org.checkerframework.checker.index.qual.LengthOf

class ForumFragment : Fragment() {

    private var _binding: FragmentForumBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForumBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.forumItemList
        recyclerView.layoutManager = LinearLayoutManager(context)

        DB.instance.fetchGlobalForum().thenAccept { forum ->
            activity?.runOnUiThread {
                Log.d(TAG, "Forum: $forum")
                DB.instance.fetchForumItems(forum.getId()).thenAccept { forumItems ->
                    activity?.runOnUiThread {
                        Log.d(TAG, "Forum items: $forumItems")
                        binding.forumItemList.adapter = ForumItemAdapter(forumItems, forum.getId(), findNavController())
                    }
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ForumFragment"
    }
}