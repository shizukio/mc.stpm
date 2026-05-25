# Simple Teleport Point Marker

Minecraft サーバー上でテレポート用の marker point を保存し、プレイヤーをその地点へ移動できるようにするプラグインです。

このプロジェクトは、複数の loader に対応しやすいように module を分割しています。

## Module 構成

`stpm-core`

loader に依存しない共通コードだけを置きます。

Bukkit、Paper、CommandAPI、Adventure など、サーバー実装や loader に依存する API はここへ import しません。

`stpm-bukkit`

Bukkit / Spigot 互換の実装を置きます。

現在は command、marker entity の操作、config 読み込み、scheduler、Bukkit 用 plugin entry point をこの module が担当します。

`stpm-paper`

Paper 固有の entry point と、将来の Paper 専用実装を置きます。

現時点では Paper 専用 API が必要な処理はないため、Bukkit runtime を再利用しています。Paper 専用 API を使う処理を追加する場合は、この module に実装します。

## ビルド方法

Java 21 が必要です。

```powershell
.\gradlew.bat build
```

サーバーへ入れる配布用 jar は、ルートの専用ディレクトリへ出力されます。

```text
build/plugin-jars/
```

出力例:

```text
build/plugin-jars/stpm-bukkit-<version>.jar
build/plugin-jars/stpm-paper-<version>.jar
```

Bukkit / Spigot 系サーバーでは `stpm-bukkit` の jar を使います。

Paper サーバーでは `stpm-paper` の jar を使います。

## 開発ルール

- loader に依存しない状態や enum は `stpm-core` に置きます。
- Bukkit API を使う処理は `stpm-bukkit` に置きます。
- Paper 専用 API を使う処理は `stpm-paper` に置きます。
- `org.bukkit`、`io.papermc`、`dev.jorel.commandapi` を import する class は `stpm-core` に置きません。

## Paper テストサーバーの起動

```powershell
.\gradlew.bat :stpm-paper:runServer
```

`runServer` を実行すると、`dev-server/` にある共有設定が `run/` へ自動で同期されます。

現在同期している主な設定:

- `dev-server/server.properties`
- `dev-server/eula.txt`

`run/` はワールド、ログ、サーバー jar などが生成される作業ディレクトリなので git 管理しません。共有したいサーバー設定は `run/` ではなく `dev-server/` 側を編集してください。
