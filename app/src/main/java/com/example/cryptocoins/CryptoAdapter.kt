package com.example.cryptocoins

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocoins.databinding.RvItemBinding

class CryptoAdapter(
    private var data: List<CrptoModel>
) : RecyclerView.Adapter<CryptoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  ViewHolder{
        val binding = RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setAnimation(holder.itemView)
        holder.itemBinding.name.text = data[position].name
        holder.itemBinding.symbol.text = data[position].symbol
        holder.itemBinding.price.text = data[position].price
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(val itemBinding:RvItemBinding ) : RecyclerView.ViewHolder(itemBinding.root) {
        // ViewHolder implementation
    }

    fun  changeData(filterData : ArrayList<CrptoModel>){
        data = filterData
        notifyDataSetChanged()
    }

    fun setAnimation(view: View){
        val anim = AlphaAnimation(0.0f,1.0f)
        anim.duration = 1000
        view.startAnimation(anim)
    }
}



