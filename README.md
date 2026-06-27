![chiyogami](https://user-images.githubusercontent.com/34712108/135766838-98102b74-0990-4408-af3d-d576edb0b8fb.png)

Chiyogamiは [Paper](https://github.com/PaperMC/Paper) をフォークしたものであり、Spigotプラグインを動作させつつマルチスレッド実行を可能とするMinecraftサーバーソフトです。

[![Support Server](https://img.shields.io/discord/893173646728757268.svg?label=Discord&logo=Discord&colorB=7289da&style=for-the-badge)](https://discord.com/invite/KKQNAPsFR6)

> [Download](https://github.com/bea4dev/Chiyogami/releases)

Other versions
------
* [1.21.8](https://github.com/bea4dev/Chiyogami/tree/ver/1.21.8)
* [1.21.5](https://github.com/bea4dev/Chiyogami/tree/ver/1.21.5)

Notes
------
- [x] このサーバーは開発段階であるため十分なテストがされていません
- [x] このサーバーを実行する前には必ずワールドデータ等のバックアップをしてください
- [x] issue等のフィードバックを歓迎します

API
------
[ChiyogamiLib](https://github.com/bea4dev/ChiyogamiLib/tree/master)

How to build
------

ビルドを実行するには、git, jdk21が必要です。

1. リポジトリを [ダウンロード](https://codeload.github.com/bea4dev/Chiyogami/zip/refs/heads/ver/1.21) or clone して解凍します。
2. 解凍したフォルダ内でWindowsの場合はgit-bash、linux or Macの場合はターミナルを開き```./gradlew applyAllPatches```を実行したあと```./gradlew createMojmapBundlerJar```を実行します
3. ```chiyogami-server/build/libs```内にjarファイルが生成されていれば成功です

For developer
------

このサーバーはワールドにそれぞれ専用のスレッドを割り当て、楽観的に同期を取りつつ動作します。

イベントの発火処理にはデフォルトで排他制御が設けられますが、ワールド間での順序関係は保証されないため注意が必要です。
