package domain.entities

import domain.items.*

data class Player(
    var maxHealth: Int,
    var health: Int,
    var agility: Int, // ловкость
    var strength: Int, // сила
    var position: Pair<Int, Int>, // Координаты в формате (x, y)
    val backpack: Backpack = Backpack(), // Рюкзак, где храним предметы
    var isStunned: Boolean = false // Новый флаг для состояния "оглушён"
) {
    var damage: Int = 0
        get() = (strength / 2 * agility / 2) / 2
    fun pickUpItem(item: Item): Boolean {
        return backpack.addItem(item)
    }

    // Метод для исцеления
    fun heal(amount: Int) {
        health = (health + amount).coerceAtMost(maxHealth)
    }

    // Метод для увеличения максимального здоровья
    fun increaseMaxHealth(amount: Int) {
        maxHealth += amount
        health = health.coerceAtMost(maxHealth)
    }

    fun applyBuff(attribute: String, amount: Int, duration: Int) {
        when (attribute.lowercase()) {
            "agility" -> agility += amount
            "strength" -> strength += amount
            // Логика для временного эффекта и его длительности
        }
    }

    // Метод для добавления золота
    fun addGold(amount: Int) {
        // Реализация метода добавления золота к инвентарю или атрибуту игрока
        println("Gold added: $amount.")
    }

    // Метод для уменьшения максимального здоровья
    fun decreaseMaxHealth(amount: Int) {
        maxHealth = (maxHealth - amount).coerceAtLeast(1)
        health = health.coerceAtMost(maxHealth)
        println("Player's maximum health decreased to $maxHealth.")
    }

    // Метод для оглушения игрока
    fun stun() {
        isStunned = true
        println("Player is stunned and cannot act this turn!")
    }

    // Снятие оглушения (вызывается в начале следующего хода)
    fun recoverFromStun() {
        if (isStunned) {
            isStunned = false
            println("Player has recovered from being stunned.")
        }
    }

    // Перемещение игрока на новую позицию
    // xScale: Int, yScale: Int это переменные которые хранят на сколько надо двинуться по координатным осям
    // то есть, если
    // влево ->  xScale = -1, yScale = 0
    // вправо ->  xScale = 1, yScale = 0
    // вверх ->  xScale = 0, yScale = -1
    // вниз ->  xScale = 0, yScale = 1
    fun move(xScale: Int, yScale: Int) {
        if (isStunned) {
            println("Player is stunned and cannot move!")
            return
        }
        position = Pair(position.first + xScale, position.second + yScale)
    }

    // Добавление предмета в рюкзак


    fun useItem(item: Item) {
        // Проверяем, в инвентаре ли предмет
        if (backpack.inventory.contains(item)) {
            // Применяем эффект предмета
            item.applyEffect(this)

            // Удаляем предмет из инвентаря, если он потребляемый
            if (item is Food || item is Elixir || item is Scroll || item is Weapon) {
                backpack.removeItem(item)
                println("${item.type} removed from inventory.")
            }
        } else {
            println("Item not found in inventory.")
        }
    }

    // Метод для добавления предмета в рюкзак
    fun addItem(item: Item) {
        backpack.addItem(item)
        println("${item.type} added to inventory.")
    }

    // Проверка содержимого рюкзака
    fun listInventory(): Backpack {
        return backpack
    }
}

