import service.CacheService
import service.GitHubService
import ui.TerminalUI

fun main() {
    val cacheService = CacheService()
    val gitHubService = GitHubService(cacheService)
    val terminalUI = TerminalUI(gitHubService)
    terminalUI.start()
}