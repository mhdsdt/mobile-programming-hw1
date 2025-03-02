package model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Repository(
    val id: Long,
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    val description: String?,
    @SerializedName("html_url")
    val htmlUrl: String,
    val language: String?,
    @SerializedName("stargazers_count")
    val stars: Int,
    @SerializedName("forks_count")
    val forks: Int
) {
    override fun toString(): String {
        return """
            Repository: $name
            Description: ${description ?: "No description"}
            Language: ${language ?: "Not specified"}
            Stars: $stars
            Forks: $forks
            URL: $htmlUrl
        """.trimIndent()
    }
}