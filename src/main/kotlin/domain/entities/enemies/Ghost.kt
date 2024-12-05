package domain.entities.enemies

import domain.entities.Player
import domain.entities.Room
import kotlin.random.Random

// Привидение
class Ghost : Enemy(
    type = EnemyType.GHOST,
    health = 100,
    agility = 30,
    speed = 4,
    strength = 20,
    hostility = 2,
    position = Pair(0, 0)
) {

    private var invisible = false
    fun getVisibleStatus(): Boolean{
        return invisible
    }

    override fun move(room: Room): Pair<Int, Int> {
        if (invisible){
            // если призрак уже невидимый, считаем вероятность того что он станет видимым:
            if (becomeVisible()){
                // если true, то меняем состояние призрака
                invisible = !invisible
            }
        } else {
            // если призрак видимый, считаем вероятность того что он станет невидимым:
            if (becomeInvisible()){
                // если true, то меняем состояние призрака
                invisible = !invisible
            }
        }

        // телепортация в случайную координату в пределах комнаты:
        // Генерируем случайные координаты внутри комнаты
        val randomX = (room.tlX..room.trX).random()
        val randomY = (room.tlY..room.blY).random()

        return Pair(randomX, randomY)
    }

    override fun attack(target: Player) {
        println("Ghost attacks with a chilling touch.")
    }

    private fun becomeVisible(): Boolean{
        // генерируем число от 0 до 9, если число <= 6, призрак становится видимым
        return Random.nextInt(0, 10) <= 6
    }

    private fun becomeInvisible(): Boolean{
        // генерируем число от 0 до 9, если число <= 4, призрак становится невидимым
        return Random.nextInt(0, 10) <= 2
    }


}