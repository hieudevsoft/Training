package com.devapp.trainning

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.devapp.trainning.databinding.RowItemLayoutBinding


class ShowPostAdapter: RecyclerView.Adapter<ShowPostAdapter.MyViewHolder>() {
    private var data = emptyList<Post>()
    class MyViewHolder(private val binding:RowItemLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(item:Post){
            binding.data = item
            binding.executePendingBindings()
        }
        companion object{
            fun from(viewGroup: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(viewGroup.context)
                val binding = RowItemLayoutBinding.inflate(layoutInflater,viewGroup,false)
                return MyViewHolder(binding)
            }
        }
    }
    fun setData(newList:List<Post>){
        val diffUtilCallBack = PostDiffUtil(data,newList)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)
        data = newList
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])

    }

    override fun getItemCount(): Int {
        return data.size
    }

}