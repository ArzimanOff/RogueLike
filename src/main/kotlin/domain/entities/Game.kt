package domain.entities

import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.Screen
import data.GameRepositoryImpl
import domain.entities.enemies.*
import domain.items.*
import domain.usecases.GenerateCorridorsUseCase
import domain.usecases.GenerateRoomsMapUseCase
import presentation.Drawer
import java.lang.RuntimeException
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
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
            val startY = (room.tlY..room.blY).random()

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
            agility = 8,
            strength = 10,
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
        val startY = (randomRoom.tlY..randomRoom.blY).random()

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
            generateItemsInRoom(room) // Генерируем предметы в комнате
        }
    }

    private fun render() {
        // Отрисовка комнат и их содержимого
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
            when (keyStroke.character) {
                'w' -> moveUp()
                's' -> moveDown()
                'a' -> moveLeft()
                'd' -> moveRight()
                'u' -> useItem() // Метод для использования предмета
            }
            if (keyStroke.keyType == KeyType.Escape) break
        }
    }

    private fun useItem() {
        // Логика для использования предмета из инвентаря
        if (player.backpack.inventory.isNotEmpty()) {
            val itemToUse = player.backpack.inventory.first() // Пример: используем первый предмет
            player.useItem(itemToUse)
        } else {
            println("No items in inventory to use.")
        }

        println("\n ---------------\n ${player.listInventory()} \n ----------------------- \n")
    }

    private fun movePlayer(newX: Int, newY: Int) {
        val oldPosition = player.position
        val enemy: Enemy? = checkEnemyInPosition(newX, newY)

        // Проверка наличия предмета на новой позиции
        val item: Item? = checkItemInPosition(newX, newY)

        if (item != null) {
            // Если предмет найден, игрок подбирает его
            player.pickUpItem(item)
            // Удалим предмет из комнаты
            getCurrentRoom().items.remove(item)
        } else if (enemy != null) {
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





    private fun checkItemInPosition(newX: Int, newY: Int): Item? {
        for (room in roomsMap.rooms.values) {
            for (item in room.items) {
                if (item.position.first == newX && item.position.second == newY) {
                    return item
                }
            }
        }
        return null
    }


    private fun pickupItem(item: Item) {
        // Добавляем предмет в рюкзак игрока
        player.addItem(item)

        // Удаляем предмет из комнаты
        val currentRoom = getCurrentRoom()
        currentRoom.items.remove(item)

        // Выводим информацию об успешном добавлении
        println("Picked up ${item.type}!")
    }



    private fun attackEnemy(enemy: Enemy) {
        if (enemy.type == EnemyType.VAMPIRE &&
            !(enemy as Vampire).firstAttackMissed
        ) {
            println("Первая атака по Вампиру всегда мимо!")
            enemy.firstAttackMissed = true
        } else {
            if (enemy.takeDamage(player.damage)) {
                for (room in roomsMap.rooms) {
                    room.value.enemies.remove(enemy)
                }
                println("${enemy.type} был побежден!")
            } else {
                println("${enemy.type} получил урон ${player.damage}. Осталось здоровья: ${enemy.health}")
            }
        }
    }

    private fun nextGameStep() {
        for (room in roomsMap.rooms) {
            for (enemy in room.value.enemies) {
                val distance = findDistance(player, enemy)
                if (distance <= enemy.hostility + level.value/3) {
                    val newPosition = enemy.move(room.value)
                    enemy.setActiveStatus(true)
                } else {
                    enemy.setActiveStatus(false)
                }
                if (enemy.type == EnemyType.GHOST){
                    val newPosition = enemy.move(room.value)
                    if (isPositionValid(newPosition.first, newPosition.second)){
                        drawer.clearTile(enemy.position)
                        enemy.position = newPosition
                    }
                } else {
                    enemy.move(room.value)
                }
            }
        }
    }
    private fun findDistance(a: Player, b: Enemy): Int {
        //println("игрок: ${a.position} враг: $b на позиции ${b.position} ---> расстояние до игрока $d")
        return sqrt((
                abs(a.position.first - b.position.first).toDouble().pow(2.0)
                        +
                        abs(a.position.second - b.position.second).toDouble().pow(2.0))).toInt()
    }

    private fun getCurrentRoom(): Room {
        return roomsMap.rooms.values.firstOrNull { room ->
            player.position.first in room.topLeftX until room.topLeftX + room.width &&
                    player.position.second in room.topLeftY until room.topLeftY + room.height
        } ?: throw IllegalStateException("Player is not in any room")
    }



    private fun checkEnemyInPosition(newX: Int, newY: Int): Enemy? {
        var en: Enemy? = null

        for (room in roomsMap.rooms) {
            for (enemy in room.value.enemies) {
                if (enemy.position.first == newX &&
                    enemy.position.second == newY
                ) {
                    en = enemy
                }
            }
        }
        return en
    }

//    private fun checkItemInPosition(newX: Int, newY: Int): Item? {
//        val item: Item? = null
//
//        for (room in roomsMap.rooms) {
//            for (item in room.value.items) {
//                if (item.position.first == newX)
//            }
//        }
//
//        return item
//    }

    private fun isPositionValid(newX: Int, newY: Int): Boolean {
        if (newX < 0 || newX >= Drawer.MAP_WIDTH || newY < 0 || newY >= Drawer.MAP_HEIGHT) {
            return false // Если выход за пределы карты
        }

        // Проверяем валидность координат в комнатах и коридорах
        return roomsMap.checkCoordinatesValid(Pair(newX, newY)) ||
                corridors.checkCoordinatesValid(Pair(newX, newY))
    }



    private fun moveUp() {
        println("Move up")
        // Вызов методов для движения вверх
        movePlayer(
            player.position.first + 0,
            player.position.second + (-1),
        )
        nextGameStep()
        render()
    }

    private fun moveDown() {
        println("Move down")
        // Вызов методов для движения вниз
        movePlayer(
            player.position.first + 0,
            player.position.second + 1,
        )
        nextGameStep()
        render()
    }

    private fun moveLeft() {
        println("Move left")
        // Вызов методов для движения влево
        movePlayer(
            player.position.first + (-1),
            player.position.second + 0,
        )
        nextGameStep()
        render()
    }

    private fun moveRight() {
        println("Move right")
        // Вызов методов для движения вправо
        movePlayer(
            player.position.first + 1,
            player.position.second + 0,
        )
        nextGameStep()
        render()
    }

    private fun generateItemsInRoom(room: Room) {
        val itemsCount = Random.nextInt(1, 4) // Случайное количество предметов
        for (i in 0 until itemsCount) {
            val itemType = ItemType.values().random() // Случайный тип предмета

            // Генерация позиции для предмета в пределах комнаты
            val itemPosition = Pair(
                Random.nextInt(room.topLeftX, room.topLeftX + room.width),
                Random.nextInt(room.topLeftY, room.topLeftY + room.height)
            )

            // Создаем предмет с заданной позицией
            val item = when (itemType) {
                ItemType.FOOD -> Food(itemPosition, Random.nextInt(5, 20))
                ItemType.ELIXIR -> Elixir(itemPosition, "agility", Random.nextInt(1, 5), 3)
                ItemType.SCROLL -> Scroll(itemPosition, "strength", Random.nextInt(1, 10))
                ItemType.TREASURE -> Treasure(itemPosition, Random.nextInt(1, 100))
                ItemType.WEAPON -> Weapon(itemPosition, Random.nextInt(1, 20))
            }
            room.items.add(item) // Добавляем предмет в комнату
        }
    }
}





