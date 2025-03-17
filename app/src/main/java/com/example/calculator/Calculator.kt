package com.example.calculator

class Calculator() {
    fun add(ab: Int, ba: Int): Int = ab + ba
    fun subtract(ac: Int, bc: Int): Int = ac - bc
    fun mem() : Int { return -1 }
}

class ThingOne {
    fun doSomethingOne() {
        println("One")
    }
    fun doSomethingTwo() {
        println("Two")
    }
}

class ThingTwo {
    fun doSomethingOneTwice(thingOne: ThingOne) {
        thingOne.doSomethingOne()
        thingOne.doSomethingOne()
    }

    fun doSomethingOneThenTwo(thingOne: ThingOne) {
        thingOne.doSomethingOne()
        thingOne.doSomethingTwo()
    }
}

interface CallMe {
    fun callMe(str: String)
}

class Holder(val callMe: CallMe) {
    fun call(str : String) {
        callMe.callMe(str)
    }
}

interface Maker {
    fun result() : Int
}

interface Adder {
    fun add(a:Int, b:Int): Int
    fun getInt() : Int
    fun getString() : String
}

fun Maker.scooby() : String = "Scooby"

data class Team(val speed : Int, val door: Boolean)
