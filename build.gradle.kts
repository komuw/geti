// Helpful documentation
// 1. https://medium.com/@preslavrachev/kotlin-basics-create-executable-kotlin-jars-using-gradle-d17e9a8384b9
// 2. https://guides.gradle.org/building-kotlin-jvm-libraries/

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


val appName = "geti"
val appVersion = "v0.0.1"
val mainClass = "TaskKt"

plugins {
    kotlin("jvm") version "1.3.40"
}

repositories {
    jcenter() 
}

dependencies {
    implementation(kotlin("stdlib")) 
}

sourceSets {
    main {
        java {
            srcDir("src/main")
        }
    }
    test {
        java {
            srcDir("src/test")
        }
    }
}

tasks.jar {
    manifest {
        attributes(
            "appName" to appName,
            "appVersion" to appVersion,
            "Main-Class" to mainClass
        )
    }

    // This line of code recursively collects and copies all of a project's files
    // and adds them to the JAR itself. One can extend this task, to skip certain
    // files or particular types at will
    // see: https://docs.gradle.org/current/userguide/working_with_files.html#sec:creating_uber_jar_example
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

}

tasks.withType<KotlinCompile> {
    // https://kotlinlang.org/docs/reference/using-gradle.html#compiler-options
    kotlinOptions.allWarningsAsErrors = true
}

// run;
//   gradle build
//   java -jar build/libs/geti.jar


