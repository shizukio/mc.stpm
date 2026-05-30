import xyz.jpenilla.runpaper.task.RunServer
import java.net.ServerSocket

plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper")
}

dependencies {
    // warpmarker-bukkit モジュールをまるごと取り込みます。
    // Paper は Bukkit と互換性があるため、Bukkit 向けのコードがそのまま動きます。
    implementation(project(":warpmarker-bukkit"))

    // Paper API は実行時にサーバー本体が提供するので compileOnly にします。
    // jar に含めてしまうと二重定義になってエラーになります。
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

kotlin {
    // Java 21 のバイトコードを生成します。
    // Paper 1.21.x のサーバー本体が Java 21 を要求しているためです。
    jvmToolchain(21)
}

// -----------------------------------------------------------------------
// Minecraft バージョンの設定
// -----------------------------------------------------------------------
// デフォルトで使用するバージョンをここで管理します。
// タスク実行時に -PminecraftVersion=1.21.1 のように渡すと上書きできます。
//
// 使い方の例:
//   ./gradlew runServer                          → 1.21.4 で起動
//   ./gradlew runServer -PminecraftVersion=1.21.1 → 1.21.1 で起動
//   ./gradlew runPurpurServer -PminecraftVersion=1.21.3
//
// これにより「プラグインを別のマイナーバージョンでも動くか確認したい」
// というときに build.gradle.kts を書き換えずにテストできます。
val defaultMinecraftVersion = "1.21.4"

val minecraftVersion =
    providers
        .gradleProperty("minecraftVersion") // -P オプションを読む
        .getOrElse(defaultMinecraftVersion) // 未指定ならデフォルト値を使う

// -----------------------------------------------------------------------
// ディレクトリ設定
// -----------------------------------------------------------------------
// dev-server/ はすべてのサーバーで共通の設定置き場です。
//   dev-server/eula.txt          … EULA への同意フラグ
//   dev-server/server.properties … 難易度・ポート番号などのサーバー設定
//
// 実際にサーバーが起動するディレクトリは dev-server/run/ 以下に分けます。
// こうすることで Paper と Purpur が同じファイルを上書きし合う問題を防ぎます。
val devServerDir =
    rootProject.layout.projectDirectory.dir("dev-server")

// -----------------------------------------------------------------------
// port utility
// -----------------------------------------------------------------------

/**
 * 指定ポートが利用可能か確認
 *
 * true:
 *   使用可能
 *
 * false:
 *   既に他プロセスが使用中
 */
fun isPortAvailable(
    port: Int
): Boolean {

    return try {

        ServerSocket(port).use {
            true
        }

    } catch (_: Exception) {

        false
    }
}

/**
 * RunServer port guard
 *
 * 起動前に:
 * - 指定ポートの使用状況
 * を確認する
 *
 * 使用中の場合:
 * - task を SKIP
 * - Minecraft server を起動しない
 */
fun RunServer.requireAvailablePort(
    port: Int
) {

    onlyIf {

        val available =
            isPortAvailable(port)

        if (!available) {

            logger.lifecycle(
                """

                ========================================
                Minecraft server start cancelled
                ========================================

                Port $port is already in use.

                Possible causes:
                  - another Minecraft server is running
                  - another application uses this port

                ========================================

                """.trimIndent()
            )
        }

        available
    }
}

// -----------------------------------------------------------------------
// 共通設定ファイルの同期タスク (ユーティリティ関数)
// -----------------------------------------------------------------------
// dev-server/ 直下のファイルを各ローダーの run ディレクトリへコピーします。
// Gradle 組み込みの Sync タスクを使うため、コピー先に古いファイルが
// 残らないよう自動でクリーンアップしてくれます。
//
// 引数:
//   name      … タスク名 (例: "syncPaperServerConfig")
//   targetDir … コピー先ディレクトリ (例: dev-server/run/paper/)
fun TaskContainerScope.registerSyncTask(
    name: String,
    loaderName: String,
    targetDir: Provider<Directory>,
): TaskProvider<Sync> =
    register<Sync>(name) {
        group = "run server"
        description =
            "Sync common server config from dev-server/ into ${targetDir.get().asFile.name}."

        // 毎回必ず実行
        outputs.upToDateWhen {
            false
        }

        from(devServerDir) {
            // run/ ディレクトリ自体は再帰コピーしません。
            // そのままコピーすると run/paper/run/paper/... と入れ子になってしまうためです。
            exclude("run/**")

            // server.properties のみ内容を書き換える
            filesMatching("server.properties") {

                filter { line ->

                    if (line.startsWith("motd=")) {
                        // `motd` の値を取得
                        val motd =
                            line.removePrefix("motd=")

                        //  `{loader}` を `loaderName` で置き換える
                        "motd=${motd.replace("{loader}", loaderName)}"
                    } else {
                        line
                    }
                }
            }
        }

        into(targetDir)
    }

// -----------------------------------------------------------------------
// Purpur サーバー jar の保存先
// -----------------------------------------------------------------------
// バージョンごとに別ファイルとして保存します。
// 同じバージョンを再実行するときは downloadPurpurServer タスクがスキップされ、
// 無駄なダウンロードを防げます。
val purpurServerJar =
    rootProject.layout.buildDirectory.file(
        "run-server/purpur/purpur-$minecraftVersion.jar"
    )

tasks {
    // -----------------------------------------------------------------------
    // port check
    // -----------------------------------------------------------------------

    val minecraftPort = 25565

    // -----------------------------------------------------------------------
    // 実行ディレクトリ
    // -----------------------------------------------------------------------
    // Paper と Purpur でディレクトリを分けることで、
    // 一方の起動が他方の設定ファイルを上書きしなくなります。
    val paperRunDir =
        devServerDir.dir("run/paper").let { provider { it } }

    val purpurRunDir =
        devServerDir.dir("run/purpur").let { provider { it } }

    // 各ローダー向けの同期タスクを登録します。
    // runServer / runPurpurServer はこれらに dependsOn しているため、
    // 起動前に必ずコピーが完了します。
    val syncPaperServerConfig by registerSyncTask(
        name = "syncPaperServerConfig",
        loaderName = "Paper",
        targetDir = paperRunDir,
    )

    val syncPurpurServerConfig by registerSyncTask(
        name = "syncPurpurServerConfig",
        loaderName = "Purpur",
        targetDir = purpurRunDir,
    )

    // -----------------------------------------------------------------------
    // Purpur サーバー jar のダウンロード
    // -----------------------------------------------------------------------
    // Purpur は Paper 互換のサーバー実装で、追加のゲームプレイ機能を持ちます。
    // 公式 API (https://purpurmc.org) から最新ビルドを取得します。
    val downloadPurpurServer by registering {
        group = "run server"
        description =
            "Download the latest Purpur server jar for Minecraft $minecraftVersion."

        // outputs.file を宣言しておくと、すでにファイルが存在する場合に
        // Gradle がこのタスクを自動でスキップしてくれます (増分ビルド)。
        outputs.file(purpurServerJar)

        doLast {
            val outputFile =
                purpurServerJar.get().asFile

            // 保存先フォルダがなければ作成します。
            outputFile.parentFile.mkdirs()

            val downloadUrl =
                "https://api.purpurmc.org/v2/purpur/$minecraftVersion/latest/download"

            uri(downloadUrl)
                .toURL()
                .openStream()
                .use { input ->
                    outputFile
                        .outputStream()
                        .use { output ->
                            input.copyTo(output)
                        }
                }
        }
    }

    // -----------------------------------------------------------------------
    // jar タスク / shadowJar タスク
    // -----------------------------------------------------------------------
    // Minecraft プラグインは依存ライブラリも 1 つの jar にまとめて配布します。
    // これを「fat jar」または「shadow jar」と呼びます。
    // shadow プラグインが通常の jar の代わりにこの fat jar を生成します。

    jar {
        // 通常の jar には "plain" というサフィックスを付けて区別します。
        // サーバーに投入するのは shadowJar が生成するファイルだけです。
        archiveClassifier.set("plain")
    }

    shadowJar {
        // shadowJar はサフィックスなしで出力します (warpmarker-paper-x.x.x.jar)。
        archiveClassifier.set("")
        archiveBaseName.set("warpmarker-paper")

        // モジュールごとの build/libs/ を探し回らなくてよいよう、
        // ルートの専用ディレクトリに集約します。
        destinationDirectory.set(
            rootProject.layout.buildDirectory.dir("plugin-jars")
        )

        // warpmarker-bukkit も plugin.yml を持っています。
        // Paper 側の plugin.yml だけを使うため、重複分は除外します。
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    build {
        // ./gradlew build で shadowJar も一緒にビルドされるようにします。
        dependsOn(shadowJar)
    }

    processResources {
        // plugin.yml 内の ${version} プレースホルダーを
        // build.gradle.kts で定義したバージョン番号に置き換えます。
        val props = mapOf("version" to project.version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    // -----------------------------------------------------------------------
    // プラグインサーバー / Paper
    // -----------------------------------------------------------------------
    // run-paper プラグインが提供する組み込みタスクです。
    // Paper サーバーを自動でダウンロードして起動します。
    register<RunServer>("r.paper") {
        group = "run server"
        description =
            "Run a Paper server for plugin compatibility testing."

        // plugin build
        dependsOn(shadowJar)

        // plugin jar を plugins/ に投入
        pluginJars(
            shadowJar.flatMap {
                it.archiveFile
            }
        )

        // ポートの使用状況確認
        requireAvailablePort(minecraftPort)

        // 起動前
        dependsOn(
            // 共通設定ファイルを run/paper/ へ同期します。
            syncPaperServerConfig
        )

        // Paper サーバーの作業ディレクトリを指定します。
        // world/ や logs/ などのデータはここに生成されます。
        runDirectory.set(paperRunDir)

        // -PminecraftVersion で指定されたバージョン (未指定なら 1.21.4) を使います。
        minecraftVersion(minecraftVersion)

        // 開発用サーバーに割り当てるメモリです。
        // 必要に応じて変更してください。
        jvmArgs("-Xms2G", "-Xmx2G")
    }

    // -----------------------------------------------------------------------
    // プラグインサーバー / Purpur
    // -----------------------------------------------------------------------
    // Purpur は Paper と互換性があるため、同じプラグイン jar をそのまま読み込めます。
    // Paper では再現しない Purpur 固有の挙動を確認したいときに使います。
    register<RunServer>("r.purpur") {
        group = "run server"
        description =
            "Run a Purpur server for plugin compatibility testing."

        // plugin build
        dependsOn(shadowJar)

        // plugin jar を plugins/ に投入
        pluginJars(
            shadowJar.flatMap {
                it.archiveFile
            }
        )

        // ポートの使用状況確認
        requireAvailablePort(minecraftPort)

        // 起動前
        dependsOn(
            // Purpur jar のダウンロード
            downloadPurpurServer,
            // 設定ファイルの同期
            syncPurpurServerConfig
        )

        // Purpur サーバーの作業ディレクトリ。Paper とは別のフォルダです。
        runDirectory.set(purpurRunDir)

        // ダウンロード済みの Purpur jar をサーバーとして指定します。
        serverJar(purpurServerJar)

        minecraftVersion(minecraftVersion)

        jvmArgs("-Xms2G", "-Xmx2G")
    }
}