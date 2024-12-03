package domain.entities.enemies

import domain.entities.Player

// Огр
class Ogre : Enemy(
    type = EnemyType.OGRE,
    health = 150,
    agility = 20,
    speed = 2,
    strength = 100,
    hostility = 50,
    position = Pair(0, 0)
) {
    private var needsRest = false

    override fun move() {
        if (needsRest) {
            println("Ogre rests for a turn.")
            needsRest = false
        } else {
            println("Ogre stomps heavily across the room.")
        }
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
