plugins {
    java
    id("io.izzel.taboolib") version "1.20"
    id("org.jetbrains.kotlin.jvm") version "1.5.20"
}

taboolib {
    install("common",
            "common-5",
            "module-chat",
            "module-configuration",
            "module-database",
            "module-kether",
            "module-lang",
            "module-metrics",
            "module-nms",
            "module-nms-util",
            "module-effect",
            "module-navigation",
            "platform-bukkit")
    description {
        contributors {
            name("inrhor")
            desc("Minecraft Model Pet System")
        }
        dependencies {
            name("ModelEngine")
        }
        prefix("imiPetCore")
    }
    classifier = null
    version = "6.0.0-34"
}

repositories {
    mavenCentral()
    maven("https://mvn.lumine.io/repository/maven-public")
}

dependencies {
    compileOnly("ink.ptms.core:v11701:11701:mapped")
    compileOnly("ink.ptms.core:v11701:11701:universal")
    compileOnly("ink.ptms.core:v11604:11604:all")
    compileOnly("ink.ptms.core:v11600:11600:all")
    compileOnly("ink.ptms.core:v11500:11500:all")
    compileOnly("ink.ptms.core:v11400:11400:all")
    compileOnly("ink.ptms.core:v11300:11300:all")
    compileOnly("ink.ptms.core:v11200:11200:all")
    compileOnly("ink.ptms.core:v11100:11100:all")
    compileOnly("ink.ptms.core:v11000:11000:all")
    compileOnly("ink.ptms.core:v10900:10900:all")
    compileOnly("ink.ptms:Adyeshach:1.3.1@jar")
    compileOnly("com.ticxo.modelengine:api:R2.1.7")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}