# WarpMarker

Minecraft サーバー上でテレポート用の marker point を保存し、プレイヤーをその地点へ移動できるようにするプラグインです。

このプロジェクトは、複数の loader に対応しやすいように module を分割しています。

## Module 構成

`warpmarker-core`

loader に依存しない共通コードだけを置きます。

Bukkit、Paper、CommandAPI、Adventure など、サーバー実装や loader に依存する API はここへ import しません。

`warpmarker-bukkit`

Bukkit / Spigot 互換の実装を置きます。

現在は command、marker entity の操作、config 読み込み、scheduler、Bukkit 用 plugin entry point をこの module が担当します。

`warpmarker-paper`

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
build/plugin-jars/warpmarker-bukkit-<version>.jar
build/plugin-jars/warpmarker-paper-<version>.jar
```

Bukkit / Spigot 系サーバーでは `warpmarker-bukkit` の jar を使います。

Paper サーバーでは `warpmarker-paper` の jar を使います。

## 開発ルール

- loader に依存しない状態や enum は `warpmarker-core` に置きます。
- Bukkit API を使う処理は `warpmarker-bukkit` に置きます。
- Paper 専用 API を使う処理は `warpmarker-paper` に置きます。
- `org.bukkit`、`io.papermc`、`dev.jorel.commandapi` を import する class は `warpmarker-core` に置きません。

## コマンド権限

`/warpmarker` コマンド全体の permission は `config.yml` で設定できます。

```yaml
command:
  permission:
    enabled: true
    node: "warpmarker.command"
```

`enabled: true` の場合、`node` に設定した permission を持つ player / command sender だけが `/warpmarker` を使えます。

`enabled: false` にすると、permission check を行わず誰でも `/warpmarker` を使えます。

標準の permission node は `warpmarker.command` です。`plugin.yml` では `default: op` にしているため、OP は標準で `/warpmarker` を使えます。

LuckPerms で一般 player に許可する例:

```text
/lp group default permission set warpmarker.command true
```

## Paper テストサーバーの起動

```powershell
.\gradlew.bat :warpmarker-paper:r.paper
```

`runServer` は `dev-server/run/<ローダー>/` を作業ディレクトリとして使います。

そして、`dev-server` 直下のファイルは作業ディレクトリにコピーされます。以下は例

- `dev-server/server.properties`
- `dev-server/eula.txt`

共有したいサーバー設定は `dev-server/` 側を編集してください。

## Purpur テストサーバーの起動

Purpur は Paper 互換 server として扱い、`warpmarker-paper` の jar を読み込んで起動確認します。

```powershell
.\gradlew.bat :warpmarker-paper:r.purpur
```

初回実行時は Purpur の server jar を `build/run-server/purpur/` へダウンロードします。

