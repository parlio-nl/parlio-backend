package nl.parlio.api.tweedekamer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication class ParlioStembureauGraphqlApiApplication

fun main(args: Array<String>) {
    System.getProperties().setProperty("org.jooq.no-logo", "true")
    runApplication<ParlioStembureauGraphqlApiApplication>(*args)
}
