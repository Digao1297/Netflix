package br.com.netflix.model

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    val id: Int,
    @SerializedName("cover_url") val coverUrl: String,
    val title: String,
    val desc: String,
    val cast: String,
    @SerializedName("movie") val moviesSimilar: List<Movie>
)
