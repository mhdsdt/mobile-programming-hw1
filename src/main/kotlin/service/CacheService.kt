package service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.User
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class CacheService(private val cacheFilePath: String = "./src/main/resources/cache.json") {
    private val users = mutableMapOf<String, User>()
    private val gson = Gson()

    init {
        loadCacheFromFile()
    }

    fun getUserFromCache(username: String): User? {
        return users[username.lowercase()]
    }

    fun saveUserToCache(user: User) {
        users[user.login.lowercase()] = user
        saveCacheToFile()
    }

    fun getAllUsers(): List<User> {
        return users.values.toList()
    }

    private fun loadCacheFromFile() {
        val file = File(cacheFilePath)
        if (file.exists()) {
            try {
                FileReader(file).use { reader ->
                    val type = object : TypeToken<Map<String, User>>() {}.type
                    val loadedUsers: Map<String, User> = gson.fromJson(reader, type)
                    users.putAll(loadedUsers)
                }
                println("Cache loaded from file: ${users.size} users")
            } catch (e: Exception) {
                println("Error loading cache: ${e.message}")
            }
        }
    }

    private fun saveCacheToFile() {
        try {
            val file = File(cacheFilePath)
            FileWriter(file).use { writer ->
                gson.toJson(users, writer)
            }
            println("Cache saved to file")
        } catch (e: Exception) {
            println("Error saving cache: ${e.message}")
        }
    }
}