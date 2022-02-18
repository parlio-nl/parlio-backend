group = "nl.parlio"
version = "0.1.0-SNAPSHOT"

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369#focus=Comments-27-5181027.0-0
plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.diffplug.spotless) apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    pluginManager.withPlugin("java") {
        configure<JavaPluginExtension>() {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        tasks {
            withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
                kotlinOptions {
                    freeCompilerArgs = listOf("-Xjsr305=strict")
                    jvmTarget = "17"
                }
            }
        }
    }
    pluginManager.withPlugin("diffplug.spotless") {
        configure<com.diffplug.gradle.spotless.SpotlessExtension>() {
            java {
                googleJavaFormat(libs.versions.google.java.format.get())
                target("/*/src/**/*.java")
            }
        }
    }
}

val toolchainCompileVersion: String by project
val toolchainJavadocVersion: String by project
val toolchainTestVersion: String by project
val toolchainVm: String by project

val compileVersion = toolchainCompileVersion.replace(Regex.fromLiteral("^([0-9]+).*"), "\$1")
val javadocVersion = toolchainJavadocVersion.replace(Regex.fromLiteral("^([0-9]+).*"), "\$1")
val testVersion = toolchainTestVersion.replace(Regex.fromLiteral("^([0-9]+).*"), "\$1")
val vmImplementation: JvmImplementation = when (toolchainVm) {
    "hotspot" -> JvmImplementation.VENDOR_SPECIFIC
    "openj9" -> JvmImplementation.J9
    else -> throw GradleException("Unknown toolchain VM `${toolchainVm}`, should be `openj9` or `hotspot`")
}

task("printUsedVersions") {
    println("Java versions: (compile: ${compileVersion}), (test: ${testVersion}), (javadoc: ${javadocVersion})")
}
