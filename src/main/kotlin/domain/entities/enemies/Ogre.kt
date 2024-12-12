package domain.entities.enemies

import domain.entities.Player
import domain.entities.Room

// Огр
class Ogre : Enemy(
    type = EnemyType.OGRE,
    health = 400,
    agility = 10,
    speed = 2,
    strength = 100,
    hostility = 4,
    position = Pair(0, 0)
) {
    private var needsRest = false

    override fun move(room: Room): Pair<Int, Int>  {
        if (needsRest) {
            println("Ogre rests for a turn.")
            needsRest = false
        } else {
            println("Ogre stomps heavily across the room.")
        }
        return Pair(0, 0)
    }
    override fun attack(target: Player) {
        if (needsRest) {
            println("Ogre cannot attack while resting!")
            return
        }
        println("Ogre smashes the player with overwhelming force.")
        needsRest = true // После атаки отдыхает
    }
}
