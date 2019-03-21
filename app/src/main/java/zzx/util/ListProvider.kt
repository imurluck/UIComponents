package zzx.util

import zzx.model.FruitEntity

object ListProvider {

    private val fruitList = arrayOf("Apple", "Apricot",
        "Arbutus", "Banana", "Bennett", "Barbados", "Casaba", "Gooseberry",
        "Grapefruit", "Kernel", "Tangerine", "Walnut", "Watermelon")

    fun fruitList(): List<String> {
        return fruitList.asList()
    }

    fun fruitEntityList(): List<FruitEntity> {
        val fruitEntityList = mutableListOf<FruitEntity>()
        for (name in fruitList) {
            fruitEntityList.add(FruitEntity(name))
        }
        return fruitEntityList
    }
}