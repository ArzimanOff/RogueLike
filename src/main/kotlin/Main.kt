import data.MapGenerateRepositoryImpl
import domain.entities.RoomsMap
import domain.usecases.GenerateCorridorsUseCase
import domain.usecases.GenerateRoomsMapUseCase
import presentation.Presenter


fun main() {
    val screen = Presenter().screen
    val game = Game(screen)
    game.start()
}


