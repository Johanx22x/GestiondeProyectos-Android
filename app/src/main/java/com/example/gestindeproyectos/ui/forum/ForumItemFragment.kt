package com.example.gestindeproyectos.ui.forum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.gestindeproyectos.adapter.ForumItemRepliesAdapter
import com.example.gestindeproyectos.databinding.FragmentForumItemBinding

class ForumItemFragment: Fragment() {
    private var _binding: FragmentForumItemBinding? = null

    private val binding get() = _binding!!

    private lateinit var forumItemViewModel: ForumItemViewModel

    companion object {
        fun newInstance(forum: String, forumId: String, forumAuthorId: String): ForumItemFragment {
            val fragment = ForumItemFragment()

            // Pass the forum ID as an argument
            val args = Bundle()
            args.putString("forum", forum)
            args.putString("forumId", forumId)
            args.putString("forumAuthorId", forumAuthorId)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        forumItemViewModel =
            ForumItemViewModel(
                requireActivity().application,
                arguments?.getString("forumId")!!,
                arguments?.getString("forumItemId")!!,
                arguments?.getString("forumAuthorId")!!
            )

        _binding = FragmentForumItemBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val forumItemContentTextView: TextView = binding.forumItemContent
        forumItemViewModel.forumItemContent.observeForever {
            forumItemContentTextView.text = it
        }

        val forumItemAuthorNameTextView: TextView = binding.forumItemAuthorName
        forumItemViewModel.forumItemAuthorName.observeForever {
            forumItemAuthorNameTextView.text = it
        }

        val forumItemDateTextView: TextView = binding.forumItemDate
        forumItemViewModel.forumItemDate.observeForever {
            forumItemDateTextView.text = it
        }

        val forumItemRepliesRecyclerView: RecyclerView = binding.forumItemReplies
        forumItemViewModel.forumItemReplies.observeForever {
            forumItemRepliesRecyclerView.adapter = ForumItemRepliesAdapter(it)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
