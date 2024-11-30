package domain.usecases

import domain.entities.CorridorsList
import domain.entities.RoomsMap
import domain.repository.MapGenerateRepository

class GenerateCorridorsUseCase(
    private val repository: MapGenerateRepository
) {
    operator fun invoke(roomsMap: RoomsMap): CorridorsList {
        return repository.generateCorridors(roomsMap)
    }
}