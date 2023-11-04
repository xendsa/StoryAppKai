package com.kai.intermediatestatus.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kai.intermediatestatus.data.response.ListStoryItem
import com.kai.intermediatestatus.databinding.ItemStoryBinding
import com.kai.intermediatestatus.ui.detail.DetailActivity

class MainAdapter(
    private val storyList: List<ListStoryItem>
) :
    RecyclerView.Adapter<MainAdapter.ListViewHolder>() {
    inner class ListViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storyList: ListStoryItem) {
            binding.tvName.text = storyList.name
            binding.tvDescription.text = storyList.description

            Glide
                .with(itemView.context)
                .load(storyList.photoUrl)
                .fitCenter()
                .into(binding.ivStoryImage)

            binding.itemLayout.setOnClickListener {
                val toDetail = Intent(itemView.context, DetailActivity::class.java)
                toDetail.putExtra("storyItem", storyList)

                val optionCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivStoryImage, "story_image"),
                        Pair(binding.tvName, "name"),
                        Pair(binding.tvDescription, "description")
                    )
                itemView.context.startActivity(toDetail, optionCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = storyList[position]
        holder.bind(item)
    }
}
