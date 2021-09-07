package nl.avisi.anowalke.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

fun configureObjectMapper() = ObjectMapper()
        .registerModules(
                KotlinModule()
        )
