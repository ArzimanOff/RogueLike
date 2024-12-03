package domain.entities.enemies

import domain.entities.Player

// Привидение
class Ghost : Enemy(
    type = EnemyType.GHOST,
    health = 40,
    agility = 90,
    speed = 4,
    strength = 20,
    hostility = 10,
    position = Pair(0, 0)
) {
    private var invisible = false

    override fun move() {
        invisible = !invisible // Меняет состояние видимости
        if (invisible) {
            println("Ghost becomes invisible and teleports!")
        } else {
            println("Ghost moves unpredictably in the room.")
        }
    }

    override fun attack(target: Player) {
        println("Ghost attacks with a chilling touch.")
    }
}