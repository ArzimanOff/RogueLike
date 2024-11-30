package domain.entities

data class Player(
    var maxHealth: Int,
    var health: Int,
    var agility: Int, // ловкость
    var strength: Int, // сила
    var position: Pair<Int, Int>, // Координаты в формате (x, y)
    val inventory: MutableList<Item> = mutableListOf() // Рюкзак, где храним предметы
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

    // Перемещение игрока на новую позицию
    // xScale: Int, yScale: Int это переменные которые хранят на сколько надо двинуться по координатным осям
    // то есть, если
    // влево ->  xScale = -1, yScale = 0
    // вправо ->  xScale = 1, yScale = 0
    // вверх ->  xScale = 0, yScale = -1
    // вниз ->  xScale = 0, yScale = 1


    // Добавление предмета в рюкзак
    fun addItem(item: Item) {
        inventory.add(item)
    }

    // Удаление предмета из рюкзака
    fun removeItem(item: Item): Boolean {
        return inventory.remove(item)
    }

    // Проверка содержимого рюкзака
    fun listInventory(): List<Item> {
        return inventory
    }
}
