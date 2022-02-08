package br.com.netflix.model

import com.google.gson.annotations.SerializedName

data class Categories(@SerializedName("category") val categories: List<Category>)
