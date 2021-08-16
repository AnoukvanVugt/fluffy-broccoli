package nl.avisi.anowalke.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
//import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

fun configureObjectMapper() = ObjectMapper()
        .registerModules(
                KotlinModule()
//                JavaTimeModule(),
//                Jdk8Module()
        )
