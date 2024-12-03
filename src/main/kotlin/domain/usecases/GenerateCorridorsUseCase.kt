package domain.usecases

import domain.entities.CorridorsList
import domain.entities.RoomsMap
import domain.repository.GameRepository

class GenerateCorridorsUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(roomsMap: RoomsMap): CorridorsList {
        return repository.generateCorridors(roomsMap)
    }
}