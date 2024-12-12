package domain.items

import domain.entities.Player

/**
 * Класс, представляющий предмет типа Еда.
 */

class Food(
    position: Pair<Int, Int>,
    private val restoreAmount: Int
) : Item(
        ItemType.FOOD,
        position
    ) {
    override fun applyEffect(player: Player) {
        player.heal(restoreAmount)
        println("Food consumed, health restored by $restoreAmount.")
    }
}
