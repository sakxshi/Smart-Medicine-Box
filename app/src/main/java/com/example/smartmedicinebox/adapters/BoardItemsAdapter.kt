package com.example.smartmedicinebox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmedicinebox.R
import com.example.smartmedicinebox.models.Board

open class BoardItemsAdapter(private val context: Context, private var list: ArrayList<Board>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener: OnClickListener?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

       return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_board, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){

            holder.itemView.findViewById<TextView>(R.id.tv_name).text = model.medsName
            holder.itemView.findViewById<TextView>(R.id.tv_created_by).text = "Created by: ${model.createdBy}"

            holder.itemView.setOnClickListener{
                if(onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }

            }

        }
    }

    interface OnClickListener{
        fun onClick(position: Int, model: Board)
    }


    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)


}