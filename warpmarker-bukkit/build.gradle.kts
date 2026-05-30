plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
}

dependencies {
    implementation(project(":warpmarker-core"))

    // Bukkit 系 loader 向けの実装なので Spigot API で compile します。
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")

    implementation(kotlin("reflect"))

    // CommandAPI は Bukkit の command 実装なので loader 側に置きます。
    implementation("dev.jorel:commandapi-bukkit-shade:10.1.2")

    // chat message / clickable text の内部表現に使う Adventure API。
    implementation("net.kyori:adventure-api:4.20.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.20.0")
}

kotlin {
    jvmToolchain(21)
}

tasks {
    jar {
        archiveClassifier.set("plain")
    }

    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set("warpmarker-bukkit")

        // 配布・サーバー投入用 jar はルートの専用ディレクトリへ集約します。
        // module ごとの build/libs を探し回らなくてよいようにするためです。
        destinationDirectory.set(
            rootProject.layout.buildDirectory.dir("plugin-jars")
        )
    }

    build {
        dependsOn(shadowJar)
    }

    processResources {
        val props =
            mapOf(
                "version" to project.version
            )

        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
