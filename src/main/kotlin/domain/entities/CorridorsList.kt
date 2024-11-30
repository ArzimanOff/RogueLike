package domain.entities

data class CorridorsList(
    val connections: List<Corridor>,
    val corridorCoordinatesList: List<List<Pair<Int, Int>>>,
)
