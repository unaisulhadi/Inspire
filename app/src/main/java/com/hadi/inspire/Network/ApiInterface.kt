package com.hadi.inspire.Network

import com.hadi.inspire.Model.QuoteModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @get:GET("quotes/all")
    val getQuote: Call<QuoteModel>

}