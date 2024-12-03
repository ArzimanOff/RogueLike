package domain.entities.enemies

import domain.entities.Player

class Zombie : Enemy(
    type = EnemyType.ZOMBIE,
    health = 100,
    agility = 10,
    speed = 2,
    strength = 50,
    hostility = 30,
    position = Pair(0, 0)
) {
    override fun move() {
        // Логика передвижения зомби (например, медленное движение к игроку)
    }

    override fun attack(target: Player) {
    }
}