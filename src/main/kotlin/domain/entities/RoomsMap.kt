package domain.entities

/**
 * Класс хранящий параметры карты (координаты комнат и коридоров)
 *
 * Поле представляет собой сетку 3 на 3, каждая ячейка имеет координаты,
 * например верхняя левая (0, 0), а центральная (1, 1)
 *
 * @param width Ширина карты
 * @param height Высота карты (Длина)
 * @param rooms Структура хранящая объекты Room,
 * которые можно получить по ключу Pair(x, y) где x и y индексы ячейки поля.
 *
 */

data class RoomsMap(
    val width: Int,
    val height: Int,
    val rooms: MutableMap<Pair<Int, Int>, Room>,
    val roomsCoordinatesList: List<Pair<Int, Int>>
){
    fun checkCoordinatesValid(coordinates: Pair<Int, Int>): Boolean{
        println(roomsCoordinatesList)
        return roomsCoordinatesList.contains(coordinates)
    }
}