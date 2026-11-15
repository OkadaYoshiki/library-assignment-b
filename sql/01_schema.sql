-- =====================================================================
-- 図書貸出管理アプリ テーブル作成SQL
--   MySQL Workbench などで、02_data.sql より先に実行してください。
--   ※ 既存のテーブルは削除して作り直します（データも消えます）
-- =====================================================================

USE library;

-- 外部キーで参照している側（loans）から順に削除する
DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS books;

-- ---------------------------------------------------------------------
-- 書籍
-- ---------------------------------------------------------------------
CREATE TABLE books (
    id             BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '書籍ID',
    title          VARCHAR(100) NOT NULL                 COMMENT 'タイトル',
    author         VARCHAR(50)  NOT NULL                 COMMENT '著者',
    isbn           VARCHAR(13)  NOT NULL                 COMMENT 'ISBN（13桁）',
    published_year INT                                   COMMENT '出版年',
    status         VARCHAR(20)  NOT NULL DEFAULT 'AVAILABLE'
                                                         COMMENT '状態（AVAILABLE:貸出可 / LENT:貸出中）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_books_isbn (isbn)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '書籍';

-- ---------------------------------------------------------------------
-- 会員
-- ---------------------------------------------------------------------
CREATE TABLE members (
    id    BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '会員ID',
    name  VARCHAR(50)  NOT NULL                 COMMENT '氏名',
    email VARCHAR(100) NOT NULL                 COMMENT 'メールアドレス',
    PRIMARY KEY (id),
    UNIQUE KEY uk_members_email (email)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '会員';

-- ---------------------------------------------------------------------
-- 貸出
--   return_date が NULL のものが「貸出中」
-- ---------------------------------------------------------------------
CREATE TABLE loans (
    id          BIGINT NOT NULL AUTO_INCREMENT  COMMENT '貸出ID',
    book_id     BIGINT NOT NULL                 COMMENT '書籍ID',
    member_id   BIGINT NOT NULL                 COMMENT '会員ID',
    loan_date   DATE   NOT NULL                 COMMENT '貸出日',
    due_date    DATE   NOT NULL                 COMMENT '返却期限',
    return_date DATE                            COMMENT '返却日（未返却ならNULL）',
    PRIMARY KEY (id),
    CONSTRAINT fk_loans_book   FOREIGN KEY (book_id)   REFERENCES books (id),
    CONSTRAINT fk_loans_member FOREIGN KEY (member_id) REFERENCES members (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '貸出';
