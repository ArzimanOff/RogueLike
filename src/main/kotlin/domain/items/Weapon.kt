package domain.items

import domain.entities.Player

/**
 * Класс, представляющий предмет типа Оружие.
 */
class Weapon(
    position: Pair<Int, Int>,
    private val attackPower: Int
) : Item(ItemType.WEAPON, position) {

    override fun applyEffect(player: Player) {
        player.strength += attackPower
        println("Weapon equipped: strength increased by $attackPower.")
    }
}
