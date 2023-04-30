package com.example.camerascanner_docscannerpdfmaker.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.camerascanner_docscannerpdfmaker.R
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSketchFilter

class FilterAdapter(list: MutableList<Bitmap>,var onCLickItem: OnCLickItem) : RecyclerView.Adapter<FilterAdapter.MyViewHolder>() {

    lateinit var mContext : Context
    var myList = list
    var onClickItem  = onCLickItem

    interface OnCLickItem{
        fun getClick(position: Int)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.filter_adapter_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(mContext).load(myList.get(position)).into(holder.imageView)
        holder.imageView.setOnClickListener {
            onClickItem.getClick(position)
        }
    }
}