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

//class Operators {
//    fun add(m: Int, n: Int): Int = m + n
//    fun subtract(n: Int, m: Int): Int = n - m
//    fun multiply(c: Int, a: Int): Int = c * a
//    fun divide(l: Int, d: Int): Int = l / d
//}
