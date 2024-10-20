package com.cwhat.teducationandroidhw1.hw5

class PetShop {

    fun petsType(pet: Pet) = when (pet) {
        is Dog -> "Dog"
        is Cat -> "Cat"
    }

}

fun main() {
    val dog = Corgi(5, 3f)
    val cat = ScottishCat(7, 4f)

    val shop = PetShop()
    println(shop.petsType(dog)) // "Dog"
    println(shop.petsType(cat)) // "Cat"
}