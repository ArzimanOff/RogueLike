package domain.entities.enemies

import domain.entities.Player
import domain.entities.Room
import kotlin.random.Random

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

    private fun calculateHitChance(defender: Enemy, target: Player): Int {
        return Math.max(10, agility - target.agility + 50) // Базовый шанс 50%
    }

    private fun calculateDamage(defender: Enemy): Int {
        return strength + Random.nextInt(0, 5) // Добавить небольшой случайный урон
    }


    fun takeDamage(amount: Int, room: Room) {
        health = (health - amount).coerceAtLeast(0)
        if (health <= 0) {
            println("$type has been defeated!")

            // Выпадение сокровищ (можно заменить на свою логику)
            val treasure = Random.nextInt(1, 10) // Случайное количество сокровищ
            println("You found $treasure treasures!")

            // Удаление противника из комнаты
            room.enemies.remove(this) // Удаляем противника из списка врагов
        }
    }



}