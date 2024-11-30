package domain.usecases

import domain.entities.RoomsMap
import domain.repository.MapGenerateRepository

class GenerateRoomsMapUseCase(
    private val repository: MapGenerateRepository
) {
    operator fun invoke(): RoomsMap {
        return repository.generateRoomsMap()
    }
}