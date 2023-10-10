![chiyogami](https://user-images.githubusercontent.com/34712108/135766838-98102b74-0990-4408-af3d-d576edb0b8fb.png)

Chiyogamiは [Paper](https://github.com/PaperMC/Paper) をフォークしたものであり、Spigotプラグインを動作させつつマルチスレッド実行を可能とするMinecraftサーバーソフトです。

[![Support Server](https://img.shields.io/discord/893173646728757268.svg?label=Discord&logo=Discord&colorB=7289da&style=for-the-badge)](https://discord.com/invite/KKQNAPsFR6)

> [Download](https://github.com/bea4dev/Chiyogami/releases)

Other versions
------
* [1.18.2](https://github.com/bea4dev/Chiyogami/tree/ver/1.18.2)
* [1.19.2](https://github.com/bea4dev/Chiyogami/tree/ver/1.19.2)

Notes
------
- [x] このサーバーは開発段階であるため十分なテストがされていません
- [x] このサーバーを実行する前には必ずワールドデータ等のバックアップをしてください
- [x] issue等のフィードバックを歓迎します

API
------
APIは未公開です

How to build
------

ビルドを実行するには、git, jdk17が必要です。

1. リポジトリを [ダウンロード](https://codeload.github.com/bea4dev/Chiyogami/zip/refs/heads/ver/1.20.1) or clone して解凍します。
2. 解凍したフォルダ内でWindowsの場合はgit-bash、linux or Macの場合はターミナルを開き```./gradlew applyPatches```を実行したあと```./gradlew createReobfBundlerJar```を実行します
3. ```build/libs```内にjarファイルが生成されていれば成功です

For developer
------

このサーバーはワールドにそれぞれ専用のスレッドを割り当て、半ば楽観的に同期を取りつつ動作します。

コマンドやスケジューラー系統の処理はマルチスレッド化した中には含まれていないので互換性を維持し易くなっています。

イベントの発火処理にはデフォルトで排他制御が設けられますが、ワールド間での順序関係は保証されないため注意が必要です。