package com.sachin.githubclosedprs

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class PRAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<PullRequest> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.pr_item, parent, false)
        return PullRequestViewHolder(parent.context, view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PullRequestViewHolder).bindView(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPullRequestList(newList: ArrayList<PullRequest>) {
        val diffCallback = PullRequestDiffUtils(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}

class PullRequestViewHolder(
    private val context: Context,
    private val view: View
) :
    RecyclerView.ViewHolder(view) {
    fun bindView(pullRequest: PullRequest) {
        view.findViewById<TextView>(R.id.userName).text = pullRequest.user.userName
        view.findViewById<TextView>(R.id.title).text = pullRequest.title
        view.findViewById<TextView>(R.id.createdDate).text = pullRequest.createdAt.substring(0, 10)
        view.findViewById<TextView>(R.id.closedDate).text = pullRequest.closedAt.substring(0, 10)

        val link = pullRequest.user.imageUrl

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(context)
            .load(link)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(circularProgressDrawable)
            .into(view.findViewById<ImageView>(R.id.image))
    }
}