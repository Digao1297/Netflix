package br.com.netflix.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("title") val title: String,
    @SerializedName("movie") val movies: List<Movie>
)
