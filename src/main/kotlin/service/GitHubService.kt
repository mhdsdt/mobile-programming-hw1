package service

import api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.Repository
import model.User
import java.io.IOException

class GitHubService(private val cacheService: CacheService) {
    private val api = RetrofitClient.gitHubApi

    suspend fun getUserInfo(username: String): Result<User> {
        val cachedUser = cacheService.getUserFromCache(username)
        if (cachedUser != null) {
            println("Using cached data for user: $username")
            return Result.success(cachedUser)
        }

        return withContext(Dispatchers.IO) {
            try {
                val userResponse = api.getUser(username)
                if (userResponse.isSuccessful) {
                    val user = userResponse.body()!!.apply {
                        repositories = mutableListOf()
                    }

                    val reposResponse = api.getUserRepositories(username)
                    if (reposResponse.isSuccessful) {
                        reposResponse.body()?.let { repos ->
                            user.repositories.addAll(repos)
                        }
                    } else {
                        println("Failed to fetch repositories: ${reposResponse.errorBody()?.string()}")
                    }

                    cacheService.saveUserToCache(user)

                    Result.success(user)
                } else {
                    Result.failure(IOException("Error fetching user: ${userResponse.code()} - ${userResponse.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun searchUsersByUsername(query: String): List<User> {
        return cacheService.getAllUsers().filter {
            it.login.contains(query, ignoreCase = true)
        }
    }

    fun searchRepositoriesByName(query: String): List<Pair<User, Repository>> {
        val results = mutableListOf<Pair<User, Repository>>()

        cacheService.getAllUsers().forEach { user ->
            user.repositories.filter {
                it.name.contains(query, ignoreCase = true)
            }.forEach { repo ->
                results.add(user to repo)
            }
        }

        return results
    }

    fun getAllCachedUsers(): List<User> {
        return cacheService.getAllUsers()
    }
}