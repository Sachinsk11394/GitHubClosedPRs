package com.sachin.githubclosedprs

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil

class PullRequestDiffUtils (private val oldList: List<PullRequest>, private val newList: List<PullRequest>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList.get(newItemPosition).id
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition].id == newList.get(newPosition).id
    }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }
}