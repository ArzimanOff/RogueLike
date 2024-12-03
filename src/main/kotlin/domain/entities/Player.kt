package domain.entities

data class Player(
    var maxHealth: Int,
    var health: Int,
    var agility: Int, // ловкость
    var strength: Int, // сила
    var position: Pair<Int, Int>, // Координаты в формате (x, y)
    val backpack: Backpack = Backpack(), // Рюкзак, где храним предметы
    var isStunned: Boolean = false // Новый флаг для состояния "оглушён"
) {
    // Метод для исцеления
    fun heal(amount: Int) {
        health = (health + amount).coerceAtMost(maxHealth)
    }

    // Метод для увеличения максимального здоровья
    fun increaseMaxHealth(amount: Int) {
        maxHealth += amount
        health = health.coerceAtMost(maxHealth)
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
    fun addItem(item: Item) {
        backpack.addItem(item)
    }

    // Удаление предмета из рюкзака
    fun removeItem(item: Item): Boolean {
        return backpack.removeItem(item)
    }

    // Проверка содержимого рюкзака
    fun listInventory(): Backpack {
        return backpack
    }
}

