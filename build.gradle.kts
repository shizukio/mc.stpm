plugins {
    kotlin("jvm") version "2.4.0-RC" apply false
    id("com.gradleup.shadow") version "9.4.1" apply false
    id("xyz.jpenilla.run-paper") version "3.0.2" apply false
}

allprojects {
    group =
        providers.gradleProperty("group").get()

    version =
        providers.gradleProperty("version").get()

    repositories {
        mavenCentral()

        // Paper API / Paper test server.
        maven("https://repo.papermc.io/repository/maven-public/")

        // CommandAPI.
        maven("https://repo.codemc.io/repository/maven-public/")

        // Bukkit/Spigot API.
        // Bukkit 系 loader 用 module は Paper API ではなく Spigot API で compile します。
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}
