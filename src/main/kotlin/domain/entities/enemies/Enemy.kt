package domain.entities.enemies

import domain.entities.Player

// Базовый класс противника
abstract class Enemy(
    val type: EnemyType,
    var health: Int,
    val agility: Int,
    val speed: Int,
    val strength: Int,
    val hostility: Int,
    var position: Pair<Int, Int>
) {
    abstract fun move() // Метод для передвижения
    abstract fun attack(target: Player) // Метод для атаки
}