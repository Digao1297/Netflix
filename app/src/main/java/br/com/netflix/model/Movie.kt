package br.com.netflix.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    @SerializedName("cover_url") val coverUrl: String,
    val title: String,
    val desc: String,
    val cast: String
) {
    constructor(
        id: Int,
        coverUrl: String
    ) : this(id, coverUrl, "", "", "")
}
