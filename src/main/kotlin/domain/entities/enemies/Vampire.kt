package domain.entities.enemies

import domain.entities.Player
import domain.entities.Room

// Вампир
class Vampire : Enemy(
    type = EnemyType.VAMPIRE,
    health = 300,
    agility = 30,
    speed = 3,
    strength = 40,
    hostility = 7,
    position = Pair(0, 0)
) {

    var firstAttackMissed = false
    override fun move(room: Room): Pair<Int, Int>  {
        println("Vampire moves swiftly towards the player.")
        return Pair(0, 0)
    }

    override fun attack(target: Player) {
    }
}