package domain.entities

import domain.items.Item

data class Backpack(
    val inventory: MutableList<Item> = mutableListOf()
) {
    fun addItem(item: Item): Boolean{
        inventory.add(item)
        return true
    }

    fun removeItem(item: Item): Boolean{
        inventory.remove(item)
        return true
    }
}