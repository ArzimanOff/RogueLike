package domain.items

import domain.entities.Player
import kotlin.random.Random

/**
 * Класс, представляющий предмет типа Эликсир.
 */
class Elixir(
    position: Pair<Int, Int>,
    private val attribute: String,
    private val boostAmount: Int,
    private val duration: Int
) : Item(
        ItemType.ELIXIR,
        position
    ) {
        override fun applyEffect(player: Player) {
            // Применение временного эффекта к игроку
            player.applyBuff(attribute, boostAmount, duration)
            println("Elixir applied: $attribute increased by $boostAmount for $duration turns.")
        }
}
