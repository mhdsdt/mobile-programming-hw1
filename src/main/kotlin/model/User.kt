package model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class User(
    val login: String,
    val id: Long,
    val name: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("followers_url")
    val followersUrl: String,
    @SerializedName("following_url")
    val followingUrl: String,
    @SerializedName("public_repos")
    val publicRepos: Int,
    val followers: Int,
    val following: Int,
    @SerializedName("created_at")
    val createdAt: String,
    var repositories: MutableList<Repository> = mutableListOf()
) {
    private fun getFormattedCreatedAt(): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(createdAt, inputFormatter)
        return dateTime.format(outputFormatter)
    }

    override fun toString(): String {
        return """
            Username: $login
            Name: ${name ?: "Not provided"}
            Followers: $followers
            Following: $following
            Public Repositories: $publicRepos
            Account Created: ${getFormattedCreatedAt()}
        """.trimIndent()
    }
}