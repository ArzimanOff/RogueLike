package domain.entities

data class CorridorCoordinates(
    val corridor: Corridor,
    val coordinates: List<Pair<Int, Int>>
)