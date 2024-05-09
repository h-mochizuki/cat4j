# Cat4j

## 概要
このプログラムは、2024年の新人研修課題のサンプルとしてUNIXやLinuxのコマンドラインツールである `cat` をJavaで実装したものです。

ファイルや標準入力の中身を表示するために使われます。

## 使い方
使用するには、ビルド後に作成される `cat4j.zip` を解凍してください。

コマンドラインで解凍先ディレクトリに移動後、次のコマンドを実行します。

```
cd [解凍先ディレクトリ]/bin
cat4j [オプション] [ファイル名1] [ファイル名2] ...
```

もしくは `java` コマンドから実行できます。

```
cd [解凍先ディレクトリ]
java -cp lib/cat4j.jar gmochmoch.cat4j.Cat4j [オプション] [ファイル名1] [ファイル名2] ...
```

ファイル名の代わりに `-` を指定することで標準入力からの入力を表示します。

ファイル名を指定しなかった場合、またはファイル名が `-` の場合、標準入力からの入力を表示します。

## 実行例
以下は、プログラムの実行例です。

```
$ cat4j example.txt
This is an example file.
It contains some text.
$
$ cat4j - < example.txt
This is an example file.
It contains some text.
$
$ cat4j example1.txt example2.txt
This is the contents of example1.txt.
This is the contents of example2.txt.
$
$ echo "From echo." | cat4j example1.txt - example2.txt
This is the contents of example1.txt.
From echo.
This is the contents of example2.txt.
$
```

## ビルド方法

コマンドラインでプロジェクトディレクトリに移動後、次のコマンドを実行してください。

```
./gradlew build
```

`app/build/distributions` ディレクトリの下に `cat4j.zip` が作成されます。

## 動作環境
このプログラムは、以下の環境で動作を確認しています。

- openjdk 21.0.3
- Ubuntu 22.04.4

その他の環境でも動作する可能性がありますが、動作保証はしていません。

## ライセンス
このプログラムは、Apache License 2.0 ライセンスのもとで公開されています。詳細は、LICENSEファイルを参照してください。