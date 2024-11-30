import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.Screen
import data.MapGenerateRepositoryImpl
import domain.entities.CorridorsList
import domain.entities.Player
import domain.entities.Room
import domain.entities.RoomsMap
import domain.usecases.GenerateCorridorsUseCase
import domain.usecases.GenerateRoomsMapUseCase
import presentation.Drawer

class Game(private val screen: Screen) {
    private val drawer: Drawer = Drawer.getInstance(screen)
    private val repository = MapGenerateRepositoryImpl
    private val generateMapUseCase = GenerateRoomsMapUseCase(repository)
    private val generateCorridorsUseCase = GenerateCorridorsUseCase(repository)

    private lateinit var roomsMap: RoomsMap
    private lateinit var corridors: CorridorsList
    private lateinit var player: Player


    fun start() {
        // Инициализация игры
        screen.startScreen()
        generateLevel()
        drawer.drawMapBorders()
        generatePlayer()
        render()
        gameLoop()
    }

    private fun generatePlayer() {
        player = Player(
            maxHealth = 100,
            health = 100,
            agility = 10,
            strength = 15,
            position = Pair(0, 0) // Временное значение
        )
        placePlayerInRandomRoom(player, roomsMap.rooms)
    }

    private fun placePlayerInRandomRoom(player: Player, rooms: Map<Pair<Int, Int>, Room>) {
        // Выбираем случайную комнату
        val randomRoomKey = rooms.keys.random()
        val randomRoom = rooms[randomRoomKey]!!

        // Генерируем случайные координаты внутри комнаты
        val startX = (randomRoom.tlX..randomRoom.trX).random()
        val startY = (randomRoom.tlY..randomRoom.trY).random()

        // Устанавливаем начальные координаты игрока
        player.position = Pair(startX, startY)
    }


    private fun generateLevel() {
        roomsMap = generateMapUseCase()
        corridors = generateCorridorsUseCase(roomsMap)
    }

    private fun render() {
        // Отрисовка комнат
        for (r in roomsMap.rooms) {
            drawer.drawRoom(r.key, r.value)
        }

        // Отрисовка коридоров
        drawer.drawCorridors(corridors.corridorCoordinatesList)
        drawer.drawPlayer(player)
        screen.refresh()
    }

    private fun gameLoop() {
        while (true) {
            val keyStroke = screen.pollInput() ?: continue
            // влево ->  xScale = -1, yScale = 0
            // вправо ->  xScale = 1, yScale = 0
            // вверх ->  xScale = 0, yScale = -1
            // вниз ->  xScale = 0, yScale = 1
            when (keyStroke.character) {
                'w' -> moveUp()
                's' -> moveDown()
                'a' -> moveLeft()
                'd' -> moveRight()
            }
            if (keyStroke.keyType == KeyType.Escape) break
        }
    }

    private fun movePlayer(newX: Int, newY: Int) {
        val oldPosition = player.position

        // Убедимся, что новая позиция находится в пределах карты
        if (isPositionValid(newX, newY)) {
            // Очистить старую позицию
            drawer.clearTile(oldPosition)

            // Обновить координаты игрока
            player.position = Pair(newX, newY)

            // Нарисовать игрока на новой позиции
            drawer.drawPlayer(player)
        }
    }

    private fun isPositionValid(newX: Int, newY: Int): Boolean {
        return true
    }


    private fun moveUp() {
        println("Move up")
        // Вызов методов для движения вверх
        movePlayer(
            player.position.first + 0,
            player.position.second + (-1),
        )
        render()
    }

    private fun moveDown() {
        println("Move down")
        // Вызов методов для движения вниз
        movePlayer(
            player.position.first + 0,
            player.position.second + 1,
        )
        render()
    }

    private fun moveLeft() {
        println("Move left")
        // Вызов методов для движения влево
        movePlayer(
            player.position.first + (-1),
            player.position.second + 0,
        )
        render()
    }

    private fun moveRight() {
        println("Move right")
        // Вызов методов для движения вправо
        movePlayer(
            player.position.first + 1,
            player.position.second + 0,
        )
        render()
    }
}
