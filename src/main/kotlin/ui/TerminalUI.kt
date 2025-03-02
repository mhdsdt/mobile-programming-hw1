package ui

import kotlinx.coroutines.runBlocking
import model.Repository
import model.User
import service.GitHubService
import java.util.*

class TerminalUI(private val gitHubService: GitHubService) {
    private val scanner = Scanner(System.`in`)

    fun start() {
        println("Welcome to GitHub User Explorer!")

        while (true) {
            printMenu()
            val choice = readUserInput("Enter your choice: ")

            when (choice) {
                "1" -> fetchUserInfo()
                "2" -> displayCachedUsers()
                "3" -> searchUsersByUsername()
                "4" -> searchRepositoriesByName()
                "5" -> {
                    println("Exiting program. Goodbye!")
                    return
                }
                else -> println("Invalid choice. Please try again.")
            }

            println("\nPress Enter to continue...")
            scanner.nextLine()
        }
    }

    private fun printMenu() {
        println("\n===== GitHub User Explorer =====")
        println("1. Fetch user information by username")
        println("2. Display all cached users")
        println("3. Search users by username")
        println("4. Search repositories by name")
        println("5. Exit")
        println("================================")
    }

    private fun fetchUserInfo() {
        val username = readUserInput("Enter GitHub username: ")

        println("Fetching information for user: $username...")

        runBlocking {
            val result = gitHubService.getUserInfo(username)

            result.fold(
                onSuccess = { user ->
                    displayUserInfo(user)

                    if (user.repositories.isNotEmpty()) {
                        val showRepos = readUserInput("Do you want to see repositories? (y/n): ")
                        if (showRepos.equals("y", ignoreCase = true)) {
                            displayRepositories(user.repositories)
                        }
                    }
                },
                onFailure = { error ->
                    println("Error: ${error.message}")
                }
            )
        }
    }

    private fun displayCachedUsers() {
        val users = gitHubService.getAllCachedUsers()

        if (users.isEmpty()) {
            println("No users in cache.")
            return
        }

        println("Cached Users (${users.size}):")
        users.forEachIndexed { index, user ->
            println("${index + 1}. ${user.login} (${user.name ?: "No name"}) - ${user.repositories.size} repositories")
        }

        val viewDetails = readUserInput("Enter user number to view details (or 0 to return): ")
        val userIndex = viewDetails.toIntOrNull()

        if (userIndex != null && userIndex > 0 && userIndex <= users.size) {
            val selectedUser = users[userIndex - 1]
            displayUserInfo(selectedUser)

            if (selectedUser.repositories.isNotEmpty()) {
                val showRepos = readUserInput("Do you want to see repositories? (y/n): ")
                if (showRepos.equals("y", ignoreCase = true)) {
                    displayRepositories(selectedUser.repositories)
                }
            }
        }
    }

    private fun searchUsersByUsername() {
        val query = readUserInput("Enter username to search: ")
        val results = gitHubService.searchUsersByUsername(query)

        if (results.isEmpty()) {
            println("No users found matching: $query")
            return
        }

        println("Found ${results.size} users matching: $query")
        results.forEachIndexed { index, user ->
            println("${index + 1}. ${user.login} (${user.name ?: "No name"})")
        }

        val viewDetails = readUserInput("Enter user number to view details (or 0 to return): ")
        val userIndex = viewDetails.toIntOrNull()

        if (userIndex != null && userIndex > 0 && userIndex <= results.size) {
            val selectedUser = results[userIndex - 1]
            displayUserInfo(selectedUser)

            if (selectedUser.repositories.isNotEmpty()) {
                val showRepos = readUserInput("Do you want to see repositories? (y/n): ")
                if (showRepos.equals("y", ignoreCase = true)) {
                    displayRepositories(selectedUser.repositories)
                }
            }
        }
    }

    private fun searchRepositoriesByName() {
        val query = readUserInput("Enter repository name to search: ")
        val results = gitHubService.searchRepositoriesByName(query)

        if (results.isEmpty()) {
            println("No repositories found matching: $query")
            return
        }

        println("Found ${results.size} repositories matching: $query")
        results.forEachIndexed { index, (user, repo) ->
            println("${index + 1}. ${repo.name} (by ${user.login})")
        }

        val viewDetails = readUserInput("Enter repository number to view details (or 0 to return): ")
        val repoIndex = viewDetails.toIntOrNull()

        if (repoIndex != null && repoIndex > 0 && repoIndex <= results.size) {
            val (user, repo) = results[repoIndex - 1]
            println("\nRepository Details:")
            println(repo)
            println("\nOwner: ${user.login} (${user.name ?: "No name"})")
        }
    }

    private fun displayUserInfo(user: User) {
        println("\nUser Information:")
        println(user)
    }

    private fun displayRepositories(repositories: List<Repository>) {
        println("\nRepositories (${repositories.size}):")
        repositories.forEachIndexed { index, repo ->
            println("\n${index + 1}. ${repo.name}")
            println("   Description: ${repo.description ?: "No description"}")
            println("   Language: ${repo.language ?: "Not specified"}")
            println("   Stars: ${repo.stars}, Forks: ${repo.forks}")
        }
    }

    private fun readUserInput(prompt: String): String {
        print(prompt)
        return scanner.nextLine().trim()
    }
}