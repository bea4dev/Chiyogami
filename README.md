![chiyogami](https://user-images.githubusercontent.com/34712108/135766838-98102b74-0990-4408-af3d-d576edb0b8fb.png)

Chiyogami-Serverは [Tuinity](https://github.com/Tuinity/Tuinity) をフォークしたものであり、Spigotプラグインを動作させつつマルチスレッド実行を可能とするMinecraftサーバーソフトです。

[![Support Server](https://img.shields.io/discord/893173646728757268.svg?label=Discord&logo=Discord&colorB=7289da&style=for-the-badge)](https://discord.com/invite/KKQNAPsFR6)

[Download](https://github.com/Be4rJP/Chiyogami/releases)

[MonitorPlugin](https://github.com/Be4rJP/ChiyogamiMonitor)

Other version
------
* [1.16.5](https://github.com/Be4rJP/Chiyogami/tree/ver/1.16.5)
* [1.17.1](https://github.com/Be4rJP/Chiyogami/tree/ver/1.17.1)

Notes
------
- [x] このサーバーは開発段階であるため十分なテストがされていません
- [x] このサーバーを実行する前には必ずワールドデータ等のバックアップをしてください
- [x] issue等のフィードバックをお待ちしています。

API
------
このサーバーの機能を最大限生かす為のAPIが用意されています

以下から利用できます

https://github.com/Be4rJP/ChiyogamiLib

How to build
------

ビルドを実行するには、git, maven, jdk8が必要です。

1. リポジトリを [ダウンロード](https://codeload.github.com/Be4rJP/Chiyogami/zip/refs/heads/ver/1.15.2) or clone して解凍します。
2. 解凍したフォルダ上でWindowsの場合はgit-bash、linux or Macの場合はターミナルを開き```./buildChiyogami.sh```を実行します
3. ビルド途中に出てくるpaperclipのビルド失敗メッセージは無視しても構いません
4. ```Chiyogami-1.15.2.jar```が生成されていれば成功です

For developer
------

このサーバーのマルチスレッド化の仕組みを簡単に説明しておきます。

このサーバーは、通常はMinecraftServerのtick実行の中でWorldServerの処理をシングルスレッドで一つずつ実行しているのに対して並列に実行するように書き換えています。

コマンドやBukkitRunnable系の処理はマルチスレッド化した中には含まれていないので互換性を維持し易くなっています。

Eventには同時に実行されるのを防ぐためロックするように書き換えましたが、ワールドごとに実行スレッドが違うためワールド系のEventの実行順序が入れ替わる可能性があるため注意が必要です。

コードを閲覧したい場合は、ビルドした後に ```Chiyogami/Chiyogami-Server/src/``` をご覧ください。
コードを変更した場合は上記のソースを変更した後に```./rebuild.sh```を実行することでビルドができます。