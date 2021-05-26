package com.varunsoft.airquality.ui.aqlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.varunsoft.airquality.R
import com.varunsoft.airquality.data.model.CityAqiItem
import com.varunsoft.airquality.databinding.PostItemBinding
import com.varunsoft.airquality.utils.Utils
import kotlin.collections.ArrayList


class AqiCityListAdapter(var context: Context?, val clickAction:(String,String) -> Unit) : RecyclerView.Adapter<AqiCityListAdapter.PostsViewHolder>() {

    var posts:List<CityAqiItem>  = ArrayList<CityAqiItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        return PostsViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {

        holder.binding.city.setOnClickListener {
            clickAction(holder.binding.city.text.toString(), holder.binding.aqi.text.toString())
        }
        holder.bind(posts.get(position))
    }

    fun addData(posts: List<CityAqiItem>){
        this.posts = posts
        notifyDataSetChanged()
    }

    class PostsViewHolder(val binding: PostItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CityAqiItem){
            binding.apply {
                postItem = item
                time.text = Utils.getTimeAgo((item.time!!.time))

                if(item.aqi!!.toDouble() < 50) {
                    aqi.setTextColor(binding.root.context.getResources().getColor(R.color.green_good))
                }
                if(item.aqi.toDouble() >= 50 && item.aqi.toDouble() < 100){
                    aqi.setTextColor(binding.root.context.getResources().getColor(R.color.green_satisfactory))
                }
                if(item.aqi.toDouble() >= 100 && item.aqi.toDouble() < 200) {
                    aqi.setTextColor(binding.root.context.getResources().getColor(R.color.yellow_moderate))
                }
                if(item.aqi.toDouble() >= 200 && item.aqi.toDouble() < 300) {
                    aqi.setTextColor(binding.root.context.getResources().getColor(R.color.orange_poor))
                }
                if(item.aqi.toDouble() >= 300 && item.aqi.toDouble() < 400) {
                    aqi.setTextColor(binding.root.context.getResources().getColor(R.color.red_very_poor))
                }
                if(item.aqi.toDouble() >= 400 && item.aqi.toDouble() < 500) {
                    aqi.setTextColor(binding.root.context.getResources().getColor(R.color.dark_red_severe))
                }

                executePendingBindings()
            }
        }
    }
}