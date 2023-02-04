plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.56"
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
            name("AuthMe").optional(true)
            name("ModelEngine").optional(true)
            name("OrangeEngine").optional(true)
            name("GermEngine").optional(true)
            name("DragonCore").optional(true)
            name("UiUniverse").optional(true)
            name("ProtocolLib").optional(true)
        }
    }
    classifier = null
    version = "6.0.10-80"
}

repositories {
    mavenCentral()
    maven("https://repo.dmulloy2.net/repository/public/") // ProtocolLib
    maven("https://repo.codemc.org/repository/maven-public/") // authMe
}

dependencies {
    compileOnly("public:GermPlugin:4.0.3")
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11800:11800-minimize:api")
    compileOnly("ink.ptms.core:v11800:11800-minimize:mapped")
    compileOnly("ink.ptms.core:v11200:11200")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    taboo("ink.ptms:um:1.0.0-beta-20")
    compileOnly("public:ModelEngine:3.1.2")
    compileOnly("fr.xephi:authme:5.6.0-beta2")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
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

sourceSets {
    main {
        java {
            srcDir("src/main")
        }
    }
}

java {
    withSourcesJar()
}