package domain.entities

/**
 * Класс, представляющий коридор между двумя комнатами
 * @param from Координаты начальной комнаты
 * @param to Координаты конечной комнаты
 */
data class Corridor(
    val from: Pair<Int, Int>,
    val to: Pair<Int, Int>
)
