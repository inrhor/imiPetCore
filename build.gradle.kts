plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.42"
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
}

taboolib {
    install("common")
    install("common-5")
    install("module-ai")
    install("module-nms")
    install("module-nms-util")
    install("module-configuration")
    install("module-database")
    install("module-lang")
    install("module-chat")
    install("module-kether")
    install("module-metrics")
    install("platform-bukkit")
    install("expansion-command-helper")
    install("module-ui")
    install("expansion-javascript")
    description {
        contributors {
            name("inrhor")
            desc("Minecraft Pet Core")
        }
        dependencies {
            name("ModelEngine").optional(true)
            name("OrangeEngine").optional(true)
            name("GermEngine").optional(true)
        }
    }
    classifier = null
    version = "6.0.9-76"
}

repositories {
    mavenCentral()
    maven("https://mvn.lumine.io/repository/maven-public/") // model-engine
}

dependencies {
    compileOnly("public:GermPlugin:4.0.3")
    compileOnly("com.ticxo.modelengine:api:R3.0.0")
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11800:11800-minimize:api")
    compileOnly("ink.ptms.core:v11800:11800-minimize:mapped")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.tabooproject.org/repository/releases")
            credentials {
                username = project.findProperty("taboolibUsername").toString()
                password = project.findProperty("taboolibPassword").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            groupId = project.group.toString()
        }
    }
}