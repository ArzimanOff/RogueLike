package data

import domain.entities.Corridor
import domain.entities.CorridorsList
import domain.entities.Room
import domain.entities.RoomsMap
import domain.repository.MapGenerateRepository
import kotlin.math.abs

object MapGenerateRepositoryImpl : MapGenerateRepository {

    private const val MAP_WIDTH = 124 //120
    private const val MAP_HEIGHT = 46 //45
    private val random = kotlin.random.Random

    private const val CELL_WIDTH = (MAP_WIDTH - 4) / 3
    private const val CELL_HEIGHT = (MAP_HEIGHT - 4) / 3

    private const val MIN_ROOM_WIDTH = 6
    private const val MIN_ROOM_HEIGHT = 6

    override fun generateRoomsMap(): RoomsMap {
        val rooms: MutableMap<Pair<Int, Int>, Room> = mutableMapOf()
        for (i in 0..2) {
            for (j in 0..2) {
                rooms[Pair(i, j)] = generateRoom(i, j)
            }
        }
        println("ANS = ${checkRoomsMapIsCorrect(rooms)}")
        return RoomsMap(
            MAP_WIDTH,
            MAP_HEIGHT,
            rooms
        )
    }

    private fun checkRoomsMapIsCorrect(rooms: MutableMap<Pair<Int, Int>, Room>): Boolean {
        var res = true

        // Проход по каждой паре комнат
        rooms.forEach { (coords1, room1) ->
            rooms.forEach { (coords2, room2) ->
                if (coords1 != coords2) {
                    res = areRoomsTouching(room1, room2)
                }
            }
        }
        return res
    }

    private fun areRoomsTouching(room1: Room, room2: Room): Boolean {
        // Условие соприкосновения
        val horizontallyTouching = (room1.right == room2.left || room1.left == room2.right) &&
                (room1.top >= room2.bottom && room1.bottom <= room2.top)

        val verticallyTouching = (room1.top == room2.bottom || room1.bottom == room2.top) &&
                (room1.right >= room2.left && room1.left <= room2.right)

        return horizontallyTouching || verticallyTouching
    }

    private fun areRoomsAdjacent(room1: Room, room2: Room): Boolean {
        val corners1 = listOf(room1.btl, room1.btr, room1.bbl, room1.bbr)
        val corners2 = listOf(room2.btl, room2.btr, room2.bbl, room2.bbr)

        for (corner1 in corners1) {
            for (corner2 in corners2) {
                if (isAdjacent(corner1, corner2)) {
                    return false // Если два угла соприкасаются, возвращаем false
                }
            }
        }
        return true // Если ни одни углы не соприкасаются, возвращаем true
    }

    private fun isAdjacent(corner1: Pair<Int, Int>, corner2: Pair<Int, Int>): Boolean {
        val dx = abs(corner1.first - corner2.first)
        val dy = abs(corner1.second - corner2.second)
        return dx <= 1 && dy <= 1 // Углы соприкасаются, если их расстояние по x и y <= 1
    }

    private fun generateRoom(x: Int, y: Int): Room {
        val a = random.nextInt(0, CELL_WIDTH - MIN_ROOM_WIDTH)
        val b = random.nextInt(0, CELL_HEIGHT - MIN_ROOM_HEIGHT)
        return Room(
            x * CELL_WIDTH + 2 + x + (a),
            y * CELL_HEIGHT + 2 + y + (b),
            random.nextInt(MIN_ROOM_WIDTH, CELL_WIDTH - a),
            random.nextInt(MIN_ROOM_HEIGHT, CELL_HEIGHT - b)
        )
    }


    override fun generateCorridors(roomsMap: RoomsMap): CorridorsList {

        val rooms = roomsMap.rooms.keys
        val edges = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>() // Set для уникальности связей

        // Собираем все возможные соседние связи
        rooms.forEach { (x, y) ->
            val neighbors = listOf(
                Pair(x, y - 1), // Верхняя комната
                Pair(x, y + 1), // Нижняя комната
                Pair(x - 1, y), // Левая комната
                Pair(x + 1, y)  // Правая комната
            ).filter { it in rooms } // Только существующие комнаты
            neighbors.forEach { neighbor ->
                val from = Pair(x, y)
                val to = neighbor
                val sortedEdge = if (from.first < to.first || (from.first == to.first && from.second < to.second)) {
                    Pair(from, to)
                } else {
                    Pair(to, from)
                }
                edges.add(sortedEdge)
            }
        }

        // Перемешиваем связи
        val shuffledEdges = edges.shuffled()

        // Используем алгоритм Краскала для минимального остовного дерева
        val parent = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        rooms.forEach { parent[it] = it }

        fun find(node: Pair<Int, Int>): Pair<Int, Int> {
            if (parent[node] != node) parent[node] = find(parent[node]!!)
            return parent[node]!!
        }

        fun union(a: Pair<Int, Int>, b: Pair<Int, Int>) {
            parent[find(a)] = find(b)
        }

        val connections = mutableListOf<Corridor>()
        shuffledEdges.forEach { (from, to) ->
            if (find(from) != find(to)) {
                connections.add(Corridor(from, to))
                union(from, to)
            }
        }

        // Опционально добавляем дополнительные связи для случайности
        shuffledEdges.forEach { (from, to) ->
            if (!connections.contains(Corridor(from, to)) && random.nextBoolean()) {
                connections.add(Corridor(from, to))
            }
        }

        val corridorCoordinatesList = mutableListOf<List<Pair<Int, Int>>>()
        for (connection in connections) {
            corridorCoordinatesList.add(generateCoordinates(connection, roomsMap))
        }

        return CorridorsList(connections, corridorCoordinatesList)
    }

    private fun generateCoordinates(connection: Corridor, roomsMap: RoomsMap): List<Pair<Int, Int>> {
        val coordinates = mutableListOf<Pair<Int, Int>>()
        val from = connection.from
        val to = connection.to


        // connection = Corridor(from=(1, 1), to=(1, 2))
        // roomsMap.rooms[from] -> roomsMap.rooms[to]

        // нужно определить относительное положение комнат from и to,
        // чтобы понимать с каких границ комнат строить коридор

        if (from.first < to.first) {
            // коридор строится от правой границы первой комнаты к левой границе второй

            val fromRoom = roomsMap.rooms[from]!!
            val toRoom = roomsMap.rooms[to]!!

            // сгенерируем начальную точку
            val startX = fromRoom.trX + 1
            val startY = fromRoom.trY + random.nextInt(1, fromRoom.height - 1)

            // сгенерируем конечную точку
            val finishX = toRoom.tlX - 1
            val finishY = toRoom.tlY + random.nextInt(1, toRoom.height - 1)


            val fromTempX =
                if (toRoom.btl.first - fromRoom.btr.first >= 4) {
                    startX + ((toRoom.tlX - fromRoom.brX) / 2)
                } else {
                    toRoom.btl.first + 1
                }

            coordinates.add(Pair(startX, startY))
            var currX = startX
            var currY = startY
            if (startY == finishY) {
                while (currX != finishX) {
                    coordinates.add(Pair(currX, currY))
                    currX++
                }
            } else {
                while (currX != fromTempX) {
                    coordinates.add(Pair(currX, currY))
                    currX++
                }
                while (currY != finishY) {
                    coordinates.add(Pair(currX, currY))
                    if (startY <= finishY) {
                        currY++
                    } else {
                        currY--
                    }
                }
                while (currX < toRoom.blX) {
                    coordinates.add(Pair(currX, currY))
                    currX++
                }
            }
            coordinates.add(Pair(finishX, finishY))

        } else if (from.first == to.first) {
            // коридор строится от нижней границы первой комнаты к верхней границе второй
            val fromRoom = roomsMap.rooms[from]!!
            val toRoom = roomsMap.rooms[to]!!

            // сгенерируем начальную точку
            val startX = random.nextInt(fromRoom.blX + 1, fromRoom.brX - 1)
            val startY = fromRoom.blY + 1

            // сгенерируем конечную точку
            val finishX = random.nextInt(toRoom.tlX + 1, toRoom.trX - 1)
            val finishY = toRoom.tlY - 1

            val fromTempY =
                if (toRoom.btl.second - fromRoom.bbl.second >= 4) {
                    startY + ((toRoom.tlY - fromRoom.blY) / 2)
                } else {
                    fromRoom.bbl.second + 1
                }


            val toTempX = finishX
            coordinates.add(Pair(startX, startY))
            var currX = startX
            var currY = startY
            if (startX == finishX) {
                while (currY != finishY) {
                    coordinates.add(Pair(currX, currY))
                    currY++
                }
            } else {
                while (currY != fromTempY) {
                    coordinates.add(Pair(currX, currY))
                    currY++
                }
                while (currX != toTempX) {
                    coordinates.add(Pair(currX, currY))
                    if (startX >= finishX) {
                        currX--
                    } else {
                        currX++
                    }
                }
                while (currY != finishY) {
                    coordinates.add(Pair(currX, currY))
                    currY++
                }
            }

            coordinates.add(Pair(finishX, finishY))
        }

        println("from:")
        println(from)
        println("to: ")
        println(to)
        println(coordinates)

        return coordinates
    }


}