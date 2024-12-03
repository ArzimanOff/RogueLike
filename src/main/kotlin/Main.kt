import domain.entities.Game
import presentation.Presenter

fun main() {
    val screen = Presenter().screen
    val game = Game(screen)
    game.start()
}