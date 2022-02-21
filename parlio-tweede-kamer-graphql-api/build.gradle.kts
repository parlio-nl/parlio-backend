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
    idea
}

group = "nl.parlio.api.stembureau"

idea {
    module {
        generatedSourceDirs.plusAssign(file("src/main/jooq"))
    }
}

val integrationTestImplementation: Configuration by configurations.creating
integrationTestImplementation.extendsFrom(configurations.implementation.get())

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
    integrationTestImplementation(libs.spring.boot.starter.test)
    integrationTestImplementation(platform(libs.testcontainers.bom))
    integrationTestImplementation(libs.testcontainers.junit.jupiter)
    integrationTestImplementation(libs.testcontainers.postgresql)
//    testImplementation(libs.jfrunit.core)
}

tasks {
    generateJava {
        language = "java"
//        generateInterfaces = false // Node.id requires null (String in Kotlin), even if `kotlinAllFieldsOptional = true`
        packageName = "nl.parlio.tweedekamer.gen.graphql"
        typeMapping = mutableMapOf(
            "PersonGiftConnection" to "graphql.relay.Connection<PersonGift>",
            "PersonTripConnection" to "graphql.relay.Connection<PersonTrip>"
        )
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
                        isComments = false
                        isDeprecated = false
                        isFluentSetters = false
                        isGlobalCatalogReferences = false
                        isGlobalSchemaReferences = false
                        isGlobalTableReferences = false
                        isImmutablePojos = false
                        isIndexes = false
                        isKeys = false
                        isPojos = false
                        isRecords = true
                        isSequences = false
                    }
                    target.apply {
                        packageName = "nl.parlio.tweedekamer.gen.jooq"
                        directory = "src/main/jooq/"
                    }
                    strategy.name = "nl.parlio.ext.jooq.codegen.QPrefixGeneratorStrategy"
                }
            }
        }
    }
}

val jooqTask = tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
    enabled = System.getenv("CI") == null
    inputs.files(fileTree("src/main/resources/db/migration/postgresql").apply {
        include("*.sql")
    }).withPropertyName("migrations")
        .withPathSensitivity(PathSensitivity.RELATIVE)
    allInputsDeclared.set(true)
}

spotless {
    java {
        targetExclude("/src/main/jooq/")
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
        val integrationTest by registering(JvmTestSuite::class) {
            useJUnitJupiter()
            testType.set(TestSuiteType.INTEGRATION_TEST)
            dependencies {
                implementation(project)
            }
            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }
    }
}

tasks {
    named("check") {
        dependsOn(testing.suites.named("integrationTest"))
    }

    withType<Test> {
        systemProperty("org.jooq.no-logo", "true")
    }
}
