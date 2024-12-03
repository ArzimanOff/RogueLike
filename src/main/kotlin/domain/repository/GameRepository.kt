package domain.repository

import domain.entities.CorridorsList
import domain.entities.RoomsMap

interface GameRepository {
    fun generateRoomsMap(): RoomsMap
    fun generateCorridors(roomsMap: RoomsMap):CorridorsList
}