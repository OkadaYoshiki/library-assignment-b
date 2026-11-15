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

### 1. MySQL Workbench でデータベースとテーブルを作る

1. root で接続し、データベースとユーザーを作る（最初の1回だけ）

   ```sql
   CREATE DATABASE library CHARACTER SET utf8mb4;
   CREATE USER 'library'@'localhost' IDENTIFIED BY 'library';
   GRANT ALL PRIVILEGES ON library.* TO 'library'@'localhost';
   ```

2. 「File → Open SQL Script」で `sql/01_schema.sql` を開いて ⚡（Execute）で実行 → テーブルができる
3. 同じように `sql/02_data.sql` を実行 → 初期データが入る
4. 左の SCHEMAS で `library` を右クリック →「Refresh All」。books / members / loans が見えればOK

`01_schema.sql` を開いて、どんなテーブル・列があるか読んでおきましょう。
`entity/` の Java クラスと見比べると、クラスとテーブルの対応がわかります。

### 2. Eclipse に取り込む

1. 「ファイル → インポート → Git → Git からプロジェクト（スマート・インポート付き）」→「URI のクローン」
2. URI にこのリポジトリのURL（`https://github.com/OkadaYoshiki/library-assignment-b.git`）を貼り付ける。ユーザーとパスワードは空欄のまま
3. ブランチ `main` のまま進み、保存先（普通は `ホーム/git/library-assignment-b`）をメモして「完了」
4. ライブラリのダウンロードが終わるまで待つ。赤い×が出たら、プロジェクトを右クリック →「Maven → プロジェクトの更新」

### 3. 起動する

`src/main/java/com/example/library/LibraryApplication.java` を右クリック →「実行 → Java アプリケーション」

コンソールに `Started LibraryApplication` と出たら、ブラウザで http://localhost:8080 を開きます。
止めるときはコンソールの赤い■を押してください。

### 4. テストを実行する

`src/test/java` の `LoanServiceTest.java` を右クリック →「実行 → JUnit テスト」

### 注意

- 画面で登録・削除したデータは残ります。最初の状態に戻したいときは `sql/01_schema.sql` → `sql/02_data.sql` の順にもう一度実行してください。
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
sql/
├── 01_schema.sql            テーブル作成
└── 02_data.sql              初期データ
src/main/resources
├── application.properties   DB接続などの設定
└── templates/               画面（Thymeleaf）
```

## 提出のしかた

1. アプリを■で止める
2. 「ファイル → エクスポート → 一般 → アーカイブ・ファイル」
3. `library-assignment-b` にチェックを入れ、中の `target` フォルダーのチェックだけ外す
4. `課題B_氏名.zip` という名前で保存し、講師が指定した提出先に提出する
