package presentation

import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.TextColor
import domain.entities.Player
import domain.entities.Room
import domain.entities.enemies.Enemy
import domain.entities.enemies.EnemyType
import domain.entities.enemies.Ghost
import domain.items.ItemType

class Drawer private constructor(private val screen: Screen) {

    fun drawMapBorders() {
        val textGraphics = screen.newTextGraphics()
        textGraphics.foregroundColor = TextColor.ANSI.WHITE
        textGraphics.backgroundColor = TextColor.ANSI.BLACK

        // Верхняя и нижняя границы
        for (i in 0..<MAP_WIDTH) {
            textGraphics.putString(i, 0, "-") // Верхняя граница
            textGraphics.putString(i, MAP_HEIGHT, "-") // Нижняя граница
        }

        // Левая и правая границы
        for (i in 0..<MAP_HEIGHT) {
            textGraphics.putString(0, i, "|") // Левая граница
            textGraphics.putString(MAP_WIDTH, i, "|") // Правая граница
        }
    }



    fun drawRoomContent(p: Pair<Int, Int>, room: Room) {
        val textGraphics = screen.newTextGraphics()
        textGraphics.foregroundColor = TextColor.ANSI.WHITE
        textGraphics.backgroundColor = TextColor.ANSI.BLACK

        val x = room.topLeftX
        val y = room.topLeftY
        val w = room.width
        val h = room.height

        for (b in room.border) {
            textGraphics.putString(b.first, b.second, "▓")
        }

        // Отрисовка врагов в комнате
        for (enemy in room.enemies) {
            drawEnemy(enemy)
        }

        // Отрисовка предметов в комнате
        for (item in room.items) {
            if (item.position.first in x..<(x + w) && item.position.second in y..<(y + h)) {
                textGraphics.putString(item.position.first, item.position.second, when (item.type) {
                    ItemType.FOOD -> "F"
                    ItemType.ELIXIR -> "E"
                    ItemType.SCROLL -> "S"
                    ItemType.TREASURE -> "T"
                    ItemType.WEAPON -> "W"
                    else -> "?"
                })
            }
        }

        // textGraphics.putString(x + w / 2 - 2, y + h / 2, p.toString())
    }

    private fun drawEnemy(enemy: Enemy) {
        val textGraphics = screen.newTextGraphics()
        textGraphics.foregroundColor = enemy.type.color
        if (enemy.type == EnemyType.GHOST && (enemy as Ghost).getVisibleStatus()){
            // если true, значит призрак невидим, отображать не надо
            textGraphics.putString(enemy.position.first, enemy.position.second, "`")
        } else{
            textGraphics.putString(enemy.position.first, enemy.position.second, enemy.type.symbol.toString())
        }
        textGraphics.foregroundColor = TextColor.ANSI.WHITE
    }



    fun drawCorridors(corridorCoordinatesList: List<List<Pair<Int, Int>>>) {
        val textGraphics = screen.newTextGraphics()
        textGraphics.foregroundColor = TextColor.ANSI.WHITE
        textGraphics.backgroundColor = TextColor.ANSI.BLACK


        for (corridor in corridorCoordinatesList) {
            for (coordinates in corridor) {
                textGraphics.putString(coordinates.first, coordinates.second, ".")
            }
        }
    }

    fun drawPlayer(player: Player) {
        val textGraphics = screen.newTextGraphics()
        textGraphics.foregroundColor = TextColor.ANSI.CYAN
        textGraphics.backgroundColor = TextColor.ANSI.BLACK
        textGraphics.putString(player.position.first, player.position.second, "@")
    }

    fun clearTile(position: Pair<Int, Int>) {
        val textGraphics = screen.newTextGraphics()
        textGraphics.foregroundColor = TextColor.ANSI.CYAN
        textGraphics.backgroundColor = TextColor.ANSI.BLACK
        textGraphics.putString(position.first, position.second, " ") // Очищаем символ
    }


    fun drawTemp() {
        val textGraphics = screen.newTextGraphics()
        textGraphics.foregroundColor = TextColor.ANSI.WHITE
        textGraphics.backgroundColor = TextColor.ANSI.BLACK


        for (j in 0..<124) {
            textGraphics.putString(j, 0, "+") // Верхняя граница
        }
        for (j in 0..<124) {
            textGraphics.putString(j, 15, "+") // Верхняя граница
        }
        for (j in 0..<124) {
            textGraphics.putString(j, 30, "+") // Верхняя граница
        }
        for (j in 0..<124) {
            textGraphics.putString(j, 45, "+") // Верхняя граница
        }

        for (j in 0..<46) {
            textGraphics.putString(0, j, "+") // Верхняя граница
        }
        for (j in 0..<46) {
            textGraphics.putString(41, j, "+") // Верхняя граница
        }
        for (j in 0..<46) {
            textGraphics.putString(82, j, "+") // Верхняя граница
        }
        for (j in 0..<46) {
            textGraphics.putString(123, j, "+") // Верхняя граница
        }

    }

    companion object {
        @Volatile
        private var instance: Drawer? = null
        const val MAP_WIDTH = 124 //120
        const val MAP_HEIGHT = 46 //45

        // Метод получения единственного экземпляра
        fun getInstance(screen: Screen): Drawer {
            return instance ?: synchronized(this) {
                instance ?: Drawer(screen).also { instance = it }
            }
        }
    }
}
