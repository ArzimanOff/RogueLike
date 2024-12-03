package domain.usecases

import domain.entities.RoomsMap
import domain.repository.GameRepository

class GenerateRoomsMapUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(): RoomsMap {
        return repository.generateRoomsMap()
    }
}