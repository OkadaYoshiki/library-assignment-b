# 図書貸出管理アプリ 課題B【応用】

小さな図書館の「書籍・会員・貸出」を管理する Web アプリです。
いくつかの機能が未実装になっているので、自分で実装して完成させましょう。

**課題の内容は [`課題B.md`](課題B.md) を読んでください。**

## 使用技術

- Java 17 以上
- Spring Boot 3.3（Spring Web / Thymeleaf / Spring Data JPA / Validation）
- MySQL 8
- Eclipse（Maven プロジェクトとして使います）

## 機能一覧

| No | 機能 | URL | 状態 |
|---|---|---|---|
| ① | 書籍一覧 | GET `/books` | 実装済み |
| ② | 書籍検索 | GET `/books?keyword=...` | 実装済み |
| ③ | 書籍登録 | GET/POST `/books/new` | 実装済み |
| ④ | 書籍編集 | GET/POST `/books/{id}/edit` | 実装済み |
| ⑤ | 書籍削除 | POST `/books/{id}/delete` | 実装済み |
| ⑥ | 会員一覧 | GET `/members` | 実装済み |
| ⑦ | 会員登録 | GET/POST `/members/new` | 実装済み |
| ⑧ | 貸出 | GET/POST `/loans/new` | **未実装（課題B-1）** |
| ⑨ | 返却 | POST `/loans/{id}/return` | **未実装（課題B-2）** |
| ⑩ | 延滞一覧 | GET `/loans/overdue` | **未実装（課題B-3）** |

---

## セットアップ

### 1. MySQL Workbench でデータベースを作る

Workbench で root として接続し、次の SQL を実行します。

```sql
CREATE DATABASE library CHARACTER SET utf8mb4;
CREATE USER 'library'@'localhost' IDENTIFIED BY 'library';
GRANT ALL PRIVILEGES ON library.* TO 'library'@'localhost';
```

### 2. Eclipse に取り込む

1. このリポジトリを clone する
   - Eclipse の場合：「ファイル → インポート → Git → Git からプロジェクト → URI のクローン」
2. 「ファイル → インポート → Maven → 既存 Maven プロジェクト」で clone したフォルダを指定
3. ライブラリのダウンロードが終わるまで待つ。赤い×が出たら、プロジェクトを右クリック →「Maven → プロジェクトの更新」

### 3. 起動する

`src/main/java/com/example/library/LibraryApplication.java` を右クリック →「実行 → Java アプリケーション」

コンソールに `Started LibraryApplication` と出たら、ブラウザで http://localhost:8080 を開きます。
止めるときはコンソールの赤い■を押してください。

### 4. テストを実行する

`src/test/java` の `LoanServiceTest.java` を右クリック →「実行 → JUnit テスト」

### 注意

- 起動するたびにテーブルが作り直され、初期データに戻ります（`application.properties` の `ddl-auto=create`）。
- 未実装の機能の画面を開くと、エラー画面（404 や 500）になることがあります。実装すれば表示されるようになります。

## フォルダ構成

```
src/main/java/com/example/library
├── controller/   画面のURLと処理の対応
├── service/      業務ロジック
├── repository/   DBアクセス（Spring Data JPA）
├── entity/       テーブルに対応するクラス
├── form/         画面の入力値を受け取るクラス
└── exception/    業務エラーの例外
src/main/resources
├── application.properties   DB接続などの設定
├── data.sql                 初期データ
└── templates/               画面（Thymeleaf）
```

## 提出のしかた

自分のアカウントにこのリポジトリを fork（または自分のリポジトリに push）し、
課題ごとにコミットしてください（例：「課題B-1 書籍検索を実装」）。
