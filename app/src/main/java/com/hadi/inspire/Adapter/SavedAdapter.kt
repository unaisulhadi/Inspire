package com.hadi.inspire.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.hadi.inspire.Activity.MainActivity
import com.hadi.inspire.Activity.SavedActivity.Companion.saved_list
import com.hadi.inspire.Activity.SavedViewActivity
import com.hadi.inspire.Components.Quote
import com.hadi.inspire.R
import com.hadi.inspire.Utils.ColorStore
import com.hadi.inspire.Utils.ColorStore.colorList
import kotlinx.android.synthetic.main.item_saved.view.*


class SavedAdapter (val context: Context,val list:List<Quote>) : RecyclerView.Adapter<SavedAdapter.SavedViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_saved,parent,false)
        return SavedViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {

        holder.bg.setBackgroundColor(ContextCompat.getColor(context,R.color.md_blue_400))
        holder.quote.text = list.get(position).quote
        holder.auth.text = "- ${list.get(position).author}"



        holder.bg.setOnClickListener {

            val intent = Intent(context,SavedViewActivity::class.java)
            intent.putExtra("QUOTE",list[position])

            Log.d("SAVEDDD__",list[position].toString())

//            val pairbg = Pair.create<View,String>(holder.bg,"quoteBg")
            val pairText = Pair.create<View,String>(holder.quote,"quoteText")
            val pairAuth = Pair.create<View,String>(holder.auth,"quoteAuthor")

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,pairText,pairAuth)
            context.startActivity(intent,options.toBundle())


        }
    }

    class SavedViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val bg = itemView.sv_bg
        val quote = itemView.quote_saved
        val auth = itemView.author_saved
    }

}