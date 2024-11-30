package domain.repository

import domain.entities.CorridorsList
import domain.entities.RoomsMap

interface MapGenerateRepository {
    fun generateRoomsMap(): RoomsMap
    fun generateCorridors(roomsMap: RoomsMap):CorridorsList
}