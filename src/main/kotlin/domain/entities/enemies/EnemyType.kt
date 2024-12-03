package domain.entities.enemies

import com.googlecode.lanterna.TextColor

enum class EnemyType(val symbol: Char, val color: TextColor.ANSI) {
    ZOMBIE('Z', TextColor.ANSI.GREEN),
    VAMPIRE('V', TextColor.ANSI.RED),
    GHOST('G', TextColor.ANSI.WHITE),
    OGRE('O', TextColor.ANSI.YELLOW),
    SNAKE_MAGE('S', TextColor.ANSI.WHITE)
}