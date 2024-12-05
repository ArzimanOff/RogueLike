package presentation

import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.TextColor
import domain.entities.Player
import domain.entities.Room
import domain.entities.enemies.Enemy
import domain.entities.enemies.EnemyType
import domain.entities.enemies.Ghost

class Drawer private constructor(private val screen: Screen) {

    fun drawMapBorders() {
        val textGraphics = screen.newTextGraphics()
        textGraphics.foregroundColor = TextColor.ANSI.WHITE
        textGraphics.backgroundColor = TextColor.ANSI.BLACK

        // Верхняя и нижняя границы
        for (i in 0..<MAP_WIDTH) {
            if ( i%10 == 0){
                textGraphics.putString(i, 0, ("_").toString())
                textGraphics.putString(i, MAP_HEIGHT, "-") // Нижняя граница

            } else {

                textGraphics.putString(i, 0, (i%10).toString()) // Верхняя граница
                textGraphics.putString(i, MAP_HEIGHT, "-") // Нижняя граница
            }
//            textGraphics.putString(i, 0, "-") // Верхняя граница
//            textGraphics.putString(i, MAP_HEIGHT, "-") // Нижняя граница
        }

        // Левая и правая границы
        for (i in 0..<MAP_HEIGHT) {
            if ( i%10 == 0){
                textGraphics.putString(0, i, "_") // Левая граница
                textGraphics.putString(MAP_WIDTH, i, "|") // Правая граница
            } else {

                textGraphics.putString(0, i, (i%10).toString()) // Левая граница
                textGraphics.putString(MAP_WIDTH, i, "|") // Правая граница
            }
//            textGraphics.putString(0, i, "|") // Левая граница
//            textGraphics.putString(MAP_WIDTH, i, "|") // Правая граница
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

        for (enemy in room.enemies){
            drawEnemy(enemy)
        }

        //textGraphics.putString(x + w / 2 - 2, y + h / 2, p.toString())
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
