package domain.entities

data class CorridorsList(
    val connections: List<Corridor>,
    val corridorCoordinatesList: List<List<Pair<Int, Int>>>,
) {
    fun checkCoordinatesValid(coordinates: Pair<Int, Int>): Boolean{
        for(list in corridorCoordinatesList){
            if (list.contains(coordinates)){
                return true
            }
        }
        return false
    }
}
