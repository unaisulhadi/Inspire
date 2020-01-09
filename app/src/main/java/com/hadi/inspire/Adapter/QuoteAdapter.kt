package com.hadi.inspire.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hadi.inspire.Model.ResultsItem
import com.hadi.inspire.R
import com.hadi.inspire.Utils.ColorStore
import com.hadi.inspire.Utils.ColorStore.colorList
import kotlinx.android.synthetic.main.item_quote.view.*
import java.util.*
import kotlin.collections.ArrayList

class QuoteAdapter(
    val context: Context,
    val list: ArrayList<ResultsItem>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {

    var i = 0;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_quote, parent, false)
        return QuoteViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {



        holder.quote.text = list[position].quoteText
        holder.quote_by.text = "- ${list[position].quoteAuthor}"


        ColorStore()

//        if(position <= colorList.size){
//            i++;
//            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context, colorList.get(i)));
//        }
//        if (position == colorList.size){
//            i=0;
//        }

        if (position == 0) {
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.md_blue_400))
        } else {
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context, colorList.random()))
        }
        holder.quote_bg.setOnClickListener {
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context, colorList.random()))
        }



    }

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quote = itemView.quote_text
        val quote_by = itemView.by_text
        val quote_bg = itemView.quote_bg
    }

    interface OnItemClickListener {
        fun onItemClick(item: ResultsItem)
    }

}