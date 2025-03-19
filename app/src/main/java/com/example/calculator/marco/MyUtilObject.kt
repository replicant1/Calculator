package com.example.calculator.marco

import java.util.UUID

/**
 * We put UUID generation in an object so we can mock it with mockkObject().
 */
object MyUtilObject {
    fun generateUUID() : String {
        return UUID.randomUUID().toString()
    }
}