package domain.entities.enemies

import domain.entities.Player

// Вампир
class Vampire : Enemy(
    type = EnemyType.VAMPIRE,
    health = 90,
    agility = 80,
    speed = 3,
    strength = 40,
    hostility = 70,
    position = Pair(0, 0)
) {
    private var firstAttackMissed = false

    override fun move() {
        println("Vampire moves swiftly towards the player.")
    }

    override fun attack(target: Player) {
        if (!firstAttackMissed) {
            firstAttackMissed = true
            println("The vampire's first attack misses!")
            return
        }
        println("Vampire drains the player's health.")
        target.decreaseMaxHealth(5) // Уменьшает максимальное здоровье игрока
    }
}