package com.cwhat.teducationandroidhw1.hw5

class Husky(
    override val age: Int,
    override val weight: Float,
    override val bite: Bite = Bite.STRAIGHT
) : Dog