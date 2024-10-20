package com.cwhat.teducationandroidhw1.hw5

class SiameseCat(
    override val age: Int,
    override val weight: Float,
    override val behavior: Behavior = Behavior.ACTIVE,
) : Cat