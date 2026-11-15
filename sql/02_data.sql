-- =====================================================================
-- 図書貸出管理アプリ 初期データ
--   01_schema.sql を実行したあとに実行してください。
--   データを最初の状態に戻したいときは、01_schema.sql → 02_data.sql の順にもう一度実行します。
-- =====================================================================

USE library;

INSERT INTO books (id, title, author, isbn, published_year, status) VALUES
 (1, 'はじめてのJava',               '山田 太郎', '9784000000011', 2021, 'AVAILABLE'),
 (2, 'Spring Boot 実践入門',          '佐藤 花子', '9784000000028', 2023, 'LENT'),
 (3, 'SQLの基本',                     '鈴木 一郎', '9784000000035', 2019, 'AVAILABLE'),
 (4, 'Webアプリケーションのしくみ',   '高橋 次郎', '9784000000042', 2020, 'LENT'),
 (5, 'オブジェクト指向設計入門',      '田中 三郎', '9784000000059', 2018, 'AVAILABLE'),
 (6, 'Gitとチーム開発',               '伊藤 美咲', '9784000000066', 2022, 'AVAILABLE'),
 (7, 'HTML/CSSデザイン入門',          '渡辺 健',   '9784000000073', 2024, 'AVAILABLE'),
 (8, 'テスト駆動開発の考え方',        '山田 太郎', '9784000000080', 2017, 'AVAILABLE');

INSERT INTO members (id, name, email) VALUES
 (1, '青木 翔',   'aoki@example.com'),
 (2, '石川 さくら', 'ishikawa@example.com'),
 (3, '上田 蓮',   'ueda@example.com');

-- 貸出データ（日付はこのSQLを実行した日を基準に作成）
--  1: 延滞中（返却期限が6日前）
--  2: 貸出中（期限内）
--  3: 返却済み（履歴）
INSERT INTO loans (id, book_id, member_id, loan_date, due_date, return_date) VALUES
 (1, 2, 1, DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_SUB(CURDATE(), INTERVAL 6 DAY), NULL),
 (2, 4, 2, DATE_SUB(CURDATE(), INTERVAL 3 DAY),  DATE_ADD(CURDATE(), INTERVAL 11 DAY), NULL),
 (3, 1, 3, DATE_SUB(CURDATE(), INTERVAL 40 DAY), DATE_SUB(CURDATE(), INTERVAL 26 DAY), DATE_SUB(CURDATE(), INTERVAL 30 DAY));
