package domain.entities.enemies

import domain.entities.Player

// Змей-маг
class SnakeMage : Enemy(
    type = EnemyType.SNAKE_MAGE,
    health = 70,
    agility = 100,
    speed = 3,
    strength = 30,
    hostility = 90,
    position = Pair(0, 0)
) {
    override fun move() {
        println("Snake Mage slithers diagonally across the room.")
    }

    override fun attack(target: Player) {
        println("Snake Mage casts a spell to attack the player.")
        if ((1..100).random() <= 20) { // 20% вероятность "усыпить" игрока
            println("Player is stunned by the Snake Mage's spell!")
            target.stun() // Предполагается, что у игрока есть метод stun()
        }
    }
}