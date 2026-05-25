plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper")
}

dependencies {
    // Paper は Bukkit API と互換があるため、Bukkit 実装を土台にします。
    // Paper 固有の差分だけをこの module に追加します。
    implementation(project(":stpm-bukkit"))

    // Paper 専用 module は Paper API で compile します。
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(21)
}

tasks {
    val syncRunServerConfig by registering(Copy::class) {
        description =
            "Copy shared development server settings into the local run directory."

        // run/ は生成物として git 管理しません。
        // 共有したい設定だけ dev-server/ に置き、runServer の直前に同期します。
        from(rootProject.layout.projectDirectory.dir("dev-server"))
        into(rootProject.layout.projectDirectory.dir("run"))
    }

    jar {
        archiveClassifier.set("plain")
    }

    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set("stpm-paper")

        // 配布・サーバー投入用 jar はルートの専用ディレクトリへ集約します。
        // module ごとの build/libs を探し回らなくてよいようにするためです。
        destinationDirectory.set(
            rootProject.layout.buildDirectory.dir("plugin-jars")
        )

        // stpm-bukkit も plugin.yml を持っています。
        // Paper jar では Paper 側の plugin.yml だけを使うため、依存 jar 側の重複分は入れません。
        duplicatesStrategy =
            DuplicatesStrategy.EXCLUDE
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

    runServer {
        dependsOn(syncRunServerConfig)

        minecraftVersion("1.21.4")
        jvmArgs("-Xms2G", "-Xmx2G")
    }
}
