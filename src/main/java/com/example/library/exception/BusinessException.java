package com.example.library.exception;

/**
 * 業務ルール違反（例：貸出中の本をさらに貸し出そうとした）を表す例外
 * メッセージはそのまま画面に表示する
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
