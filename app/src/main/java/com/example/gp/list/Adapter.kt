package com.example.gp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.gp.R

class Adapter(val onClickListener: OnClickListener) : RecyclerView.Adapter<Adapter.myViewHolder>() {
    var myList = emptyList<Disease>()

    class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.skin_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.itemView.findViewById<ImageView>(R.id.disease_img)
            .setImageResource(myList[position].image)
        holder.itemView.findViewById<TextView>(R.id.diseaseName_txt).text =
            myList[position].diseaseName.toString()
//        holder.itemView.findViewById<TextView>(R.id.diseaseDescription_txt).text =
//            myList[position].diseaseDescription.toString()

        var disease = myList[position]
        holder.itemView.setOnClickListener {
            onClickListener.onClick(disease)
        }
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    fun setData(list: List<Disease>) {
        myList = list
        notifyDataSetChanged()
    }
    class OnClickListener(val clickListener: (disease: Disease) -> Unit) {
        fun onClick(disease: Disease) = clickListener(disease)
    }
}