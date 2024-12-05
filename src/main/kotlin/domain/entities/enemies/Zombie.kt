package domain.entities.enemies

import domain.entities.Player
import domain.entities.Room

class Zombie : Enemy(
    type = EnemyType.ZOMBIE,
    health = 300,
    agility = 10,
    speed = 2,
    strength = 50,
    hostility = 4,
    position = Pair(0, 0)
) {

    override fun move(room: Room): Pair<Int, Int>  {
        // Логика передвижения зомби (например, медленное движение к игроку)
        return Pair(0, 0)
    }

    override fun attack(target: Player) {
    }
}