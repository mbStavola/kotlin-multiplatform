import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

group = "xyz.stavola"

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$embeddedKotlinVersion")
    }
}

apply {
    plugin("kotlin2js")
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    "compile"(kotlin("stdlib-js"))
}

val compileKotlin2Js: Kotlin2JsCompile by tasks
compileKotlin2Js.kotlinOptions {
    sourceMap = true
    moduleKind = "commonjs"
}