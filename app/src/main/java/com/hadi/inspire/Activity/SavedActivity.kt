package com.hadi.inspire.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hadi.inspire.Adapter.QuoteAdapter
import com.hadi.inspire.Adapter.SavedAdapter
import com.hadi.inspire.Components.AppDatabase
import com.hadi.inspire.Components.DatabaseClient
import com.hadi.inspire.Components.Quote
import com.hadi.inspire.Model.ResultsItem
import com.hadi.inspire.R
import com.hadi.inspire.Utils.MarginItemDecoration
import com.hadi.inspire.Utils.RecyclerItemClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_saved.*

class SavedActivity : AppCompatActivity() {

    lateinit var roomDb: AppDatabase
    var saved_list = mutableListOf<Quote>()
    lateinit var adapter:SavedAdapter

    companion object{
        var saved_list= arrayListOf<Quote>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        roomDb = DatabaseClient.getInstance(this).appDatabase

        rv_saved.setHasFixedSize(true)
        rv_saved.layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        rv_saved.addItemDecoration(MarginItemDecoration(10))

        getSaved()




        back_saved.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("CheckResult")
    fun getSaved(){
        roomDb.quoteDao().all.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t ->
                Log.d("SAVED_LI", ":$t ");
                saved_list = t

                adapter = SavedAdapter(this@SavedActivity,saved_list)
                rv_saved.adapter = adapter
            }
    }
}

