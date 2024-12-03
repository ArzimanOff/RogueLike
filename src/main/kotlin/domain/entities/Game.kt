package domain.entities

import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.Screen
import data.GameRepositoryImpl
import domain.entities.enemies.*
import domain.usecases.GenerateCorridorsUseCase
import domain.usecases.GenerateRoomsMapUseCase
import presentation.Drawer
import java.lang.RuntimeException
import kotlin.random.Random

class Game(private val screen: Screen) {
    private val drawer: Drawer = Drawer.getInstance(screen)
    private val repository = GameRepositoryImpl
    private val generateMapUseCase = GenerateRoomsMapUseCase(repository)
    private val generateCorridorsUseCase = GenerateCorridorsUseCase(repository)
    private val level: Level = Level()

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

    private fun generateEnemiesInRoom(room: Room): MutableList<Enemy> {
        val w = room.width
        val h = room.height
        val enemies: MutableList<Enemy> = mutableListOf()
        var enemiesCnt = 0
        if (level.value < 3) {
            if (w * h <= 50) {
                enemiesCnt = 1
            } else {
                enemiesCnt = Random.nextInt(1, 3)
            }
        } else if (level.value < 7) {

        } else if (level.value < 13) {

        } else if (level.value < 19) {

        } else {

        }

        for (i in 0..<enemiesCnt) {
            val enemyType = EnemyType.entries.random()
            val enemy: Enemy = when (enemyType) {
                EnemyType.GHOST -> {
                    Ghost()
                }

                EnemyType.OGRE -> {
                    Ogre()
                }

                EnemyType.SNAKE_MAGE -> {
                    SnakeMage()
                }

                EnemyType.VAMPIRE -> {
                    Vampire()
                }

                EnemyType.ZOMBIE -> {
                    Zombie()
                }

                else -> {
                    throw RuntimeException("Неопознанный типа врага!")
                }
            }

            // Генерируем случайные координаты внутри комнаты
            val startX = (room.tlX..room.trX).random()
            val startY = (room.tlY..room.trY).random()

            // Устанавливаем начальные координаты врага
            enemy.position = Pair(startX, startY)
            enemies.add(enemy)
        }
        return enemies
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
        generateEnemies()
    }

    private fun generateEnemies() {
        for (room in roomsMap.rooms.values) {
            room.enemies = generateEnemiesInRoom(room)
        }
    }

    private fun render() {
        // Отрисовка комнат
        for (r in roomsMap.rooms) {
            drawer.drawRoomContent(r.key, r.value)
            println("\nroom: $r")
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

        val enemy: Enemy? = checkEnemyInPosition(newX, newY)

        if (enemy != null) {
            attackEnemy(enemy)
        } else {
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

    }

    private fun attackEnemy(enemy: Enemy) {
        println("Атака на $enemy")
    }

    private fun checkEnemyInPosition(newX: Int, newY: Int): Enemy? {
        var en: Enemy? = null

        for (room in roomsMap.rooms) {
            for (enemy in room.value.enemies) {
                if (enemy.position.first == newX &&
                    enemy.position.second == newY
                ){
                    en = enemy
                }
            }
        }
        return en
    }

    private fun isPositionValid(newX: Int, newY: Int): Boolean {
        return if (
            newX > 0 && newX < Drawer.MAP_WIDTH &&
            newY > 0 && newY < Drawer.MAP_HEIGHT
        ) {
            roomsMap.checkCoordinatesValid(Pair(newX, newY)) ||
                    corridors.checkCoordinatesValid(Pair(newX, newY))
        } else {
            false
        }
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