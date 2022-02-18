@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369#focus=Comments-27-5181027.0-0
plugins {
    alias(libs.plugins.flyway)
    alias(libs.plugins.jooq.codegen)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.diffplug.spotless)
    alias(libs.plugins.dgs.codegen) // Must come after Kotlin (https://github.com/Netflix/dgs-codegen/issues/171)
    java
}

group = "nl.parlio.api.stembureau"

dependencies {
    implementation(project(":parlio-graphql-api-core"))
    implementation(libs.caffeine)
    implementation(platform(libs.dgs.bom))
    implementation(libs.dgs.pagination)
    implementation(libs.dgs.spring.boot.starter)
    implementation(libs.flyway.core)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.mapstruct)
    implementation(libs.mapstruct.spring.annotations)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.jdbc)
    implementation(libs.spring.boot.starter.jooq)
    implementation(libs.spring.boot.starter.web)
    compileOnly(libs.findbugs.jsr305) // Fix "Unknown enum constant When.MAYBE"
    annotationProcessor(libs.mapstruct.processor)
    annotationProcessor(libs.mapstruct.spring.extensions)
    runtimeOnly(libs.postgresql.driver)
    jooqGenerator(libs.postgresql.driver)
    jooqGenerator(project(":parlio-jooq"))
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(platform(libs.testcontainers.bom))
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.jfrunit.core)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    generateJava {
        language = "java"
//        generateInterfaces = false // Node.id requires null (String in Kotlin), even if `kotlinAllFieldsOptional = true`
        packageName = "nl.parlio.tweedekamer.gen.graphql"
    }

    bootRun {
        mainClass.set("nl.parlio.api.stembureau.ParlioStembureauGraphqlApiApplication")
    }
}

jooq {
    version.set(dependencyManagement.importedProperties["jooq.version"])
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)

    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)

            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/parlio-dev-3"
                    user = "postgres"
                    password = "admin"
                }
                generator.apply {
                    name = "org.jooq.codegen.JavaGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        forcedTypes.addAll(listOf(
                            org.jooq.meta.jaxb.ForcedType().apply {
                                name = "varchar"
                                includeExpression = ".*"
                                includeTypes = "JSONB?"
                            },
                            org.jooq.meta.jaxb.ForcedType().apply {
                                name = "varchar"
                                includeExpression = ".*"
                                includeTypes = "INET"
                            }
                        ))
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isPojos = false
                        isImmutablePojos = true
                        isFluentSetters = true
                        isGlobalTableReferences = false
                    }
                    target.apply {
                        packageName = "nl.parlio.tweedekamer.gen.jooq"
                        directory = "build/generated-src/jooq/main"  // default (can be omitted)
                    }
                    strategy.name = "nl.parlio.ext.jooq.codegen.QPrefixGeneratorStrategy"
                }
            }
        }
    }
}
