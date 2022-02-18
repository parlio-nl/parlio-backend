@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369#focus=Comments-27-5181027.0-0
plugins {
    alias(libs.plugins.diffplug.spotless)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    java
}

group = "nl.parlio.jooq"

dependencies {
    val jooqVersion = dependencyManagement.importedProperties["jooq.version"]
    @Suppress("GradlePackageUpdate")
    implementation("org.jooq:jooq-codegen:$jooqVersion")
}

tasks.filter {
    setOf("bootJar", "bootJarMainClassName", "bootRunMainClassName", "bootRun").contains(it.name)
}.onEach {
    it.enabled = false
}