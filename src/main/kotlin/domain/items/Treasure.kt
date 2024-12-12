package domain.items

import domain.entities.Player

/**
 * Класс, представляющий предмет типа Сокровище.
 */
class Treasure(
    position: Pair<Int, Int>,
    private val value: Int
) : Item(ItemType.TREASURE, position) {

    override fun applyEffect(player: Player) {
        player.addGold(value) // Предполагается, что у игрока есть метод addGold
        println("Treasure collected: $value gold added.")
    }
}
