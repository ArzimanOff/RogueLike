package domain.items

import domain.entities.Player

/**
 * Класс, представляющий предмет типа Свиток.
 */
class Scroll(
    position: Pair<Int, Int>,
    private val attribute: String,
    private val boostAmount: Int
) : Item(
        ItemType.SCROLL,
        position
    ) {

        override fun applyEffect(player: Player) {
            when (attribute.lowercase()) {
                "agility" -> player.agility += boostAmount
                "strength" -> player.strength += boostAmount
                "maxhealth" -> player.increaseMaxHealth(boostAmount)
                else -> {
                    println("Unknown attribute: $attribute.")
                }
            }
            println("Scroll used: $attribute increased by $boostAmount.")
        }
}
