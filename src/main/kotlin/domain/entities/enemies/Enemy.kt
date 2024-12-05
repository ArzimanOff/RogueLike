package domain.entities.enemies

import domain.entities.Player
import domain.entities.Room
import java.util.*

// Базовый класс противника
abstract class Enemy(
    val id: String = UUID.randomUUID().toString(),
    val type: EnemyType,
    var health: Int,
    val agility: Int,
    val speed: Int,
    val strength: Int,
    val hostility: Int,
    var position: Pair<Int, Int>
) {
    private var active = false
    fun getActiveStatus(): Boolean{
        return active
    }
    fun setActiveStatus(newVal: Boolean){
        active = newVal
    }
    abstract fun move(room: Room): Pair<Int, Int> // Метод для передвижения
    abstract fun attack(target: Player) // Метод для атаки
    open fun takeDamage(damage: Int): Boolean {
        health -= damage
        return health <= 0 // Возвращает true, если противник побеждён
    }
}