package com.hadi.inspire.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class QuoteModel{

	@SerializedName("count")
	private int count;

	@SerializedName("results")
	private List<ResultsItem> results;

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setResults(List<ResultsItem> results){
		this.results = results;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"QuoteModel{" + 
			"count = '" + count + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}