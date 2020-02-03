package com.hadi.inspire.Model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ResultsItem{

	@SerializedName("quoteText")
	private String quoteText;

	@SerializedName("quoteAuthor")
	private String quoteAuthor;

	@SerializedName("_id")
	private String id;

	public ResultsItem(String quoteText, String quoteAuthor, String id) {
		this.quoteText = quoteText;
		this.quoteAuthor = quoteAuthor;
		this.id = id;
	}

	public void setQuoteText(String quoteText){
		this.quoteText = quoteText;
	}

	public String getQuoteText(){
		return quoteText;
	}

	public void setQuoteAuthor(String quoteAuthor){
		this.quoteAuthor = quoteAuthor;
	}

	public String getQuoteAuthor(){
		return quoteAuthor;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

//	@Override
// 	public String toString(){
//		return
//			"ResultsItem{" +
//			"quoteText = '" + quoteText + '\'' +
//			",quoteAuthor = '" + quoteAuthor + '\'' +
//			",_id = '" + id + '\'' +
//			"}";
//		}


	@NonNull
	@Override
	public String toString() {
		return quoteText+" "+quoteAuthor;
	}
}