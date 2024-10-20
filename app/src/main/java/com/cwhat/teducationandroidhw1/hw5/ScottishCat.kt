package com.cwhat.teducationandroidhw1.hw5

class ScottishCat(
    override val age: Int,
    override val weight: Float,
    override val behavior: Behavior = Behavior.PASSIVE
) : Cat