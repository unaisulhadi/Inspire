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

        if (position == 0) {
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.md_blue_400))
        } else {
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context, colorList.random()))
        }
        holder.quote_bg.setOnClickListener {
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context, colorList.random()))
        }


        /*if(position % 40 == 0){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_blue_400))
        }else if(position % 40 == 1){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_red_300))
        }else if(position % 40 == 2){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_pink_300))
        }else if(position % 40 == 3){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_purple_300))
        }else if(position % 40 == 4){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_deep_purple_300))
        }else if(position % 40 == 5){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_indigo_300))
        }else if(position % 40 == 6){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_blue_300))
        }else if(position % 40 == 7){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_light_blue_300))
        }else if(position % 40 == 8){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_cyan_300))
        }else if(position % 40 == 9){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_teal_300))
        }else if(position % 40 == 10){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_green_300))
        }else if(position % 40 == 11){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_light_green_300))
        }else if(position % 40 == 12){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_lime_300))
        }else if(position % 40 == 13){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_yellow_300))
        }else if(position % 40 == 14){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_amber_300))
        }else if(position % 40 == 15){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_orange_300))
        }else if(position % 40 == 16){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_deep_orange_300))
        }else if(position % 40 == 17){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_brown_300))
        }else if(position % 40 == 18){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_grey_300))
        }else if(position % 40 == 19){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_blue_grey_300))
        }



        if(position % 40 == 0){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_blue_400))
        }else if(position % 40 == 1){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_red_300))
        }else if(position % 40 == 2){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_pink_300))
        }else if(position % 40 == 3){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_purple_300))
        }else if(position % 40 == 4){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_deep_purple_300))
        }else if(position % 40 == 5){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_indigo_300))
        }else if(position % 40 == 6){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_blue_300))
        }else if(position % 40 == 7){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_light_blue_300))
        }else if(position % 40 == 8){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_cyan_300))
        }else if(position % 40 == 9){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_teal_300))
        }else if(position % 40 == 10){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_green_300))
        }else if(position % 40 == 11){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_light_green_300))
        }else if(position % 40 == 12){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_lime_300))
        }else if(position % 40 == 13){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_yellow_300))
        }else if(position % 40 == 14){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_amber_300))
        }else if(position % 40 == 15){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_orange_300))
        }else if(position % 40 == 16){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_deep_orange_300))
        }else if(position % 40 == 17){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_brown_300))
        }else if(position % 40 == 18){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_grey_300))
        }else if(position % 40 == 19){
            holder.quote_bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_blue_grey_300))
        }*/
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