package domain.items


import com.googlecode.lanterna.TextColor
import domain.entities.Player

/**
 * Общий класс для всех игровых предметов.
 */

abstract class Item(
    val type: ItemType,
    var position: Pair<Int, Int>
) {
    abstract fun applyEffect(player: Player)

}


enum class ItemType(val symbol: Char, val color: TextColor.ANSI) {
    TREASURE('T', TextColor.ANSI.BLUE),
    FOOD('F', TextColor.ANSI.BLUE),
    ELIXIR('E', TextColor.ANSI.BLUE),
    SCROLL('S', TextColor.ANSI.BLUE),
    WEAPON('W', TextColor.ANSI.BLUE)
}


