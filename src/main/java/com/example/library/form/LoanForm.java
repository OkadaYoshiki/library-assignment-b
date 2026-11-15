package com.example.library.form;

import jakarta.validation.constraints.NotNull;

/**
 * 貸出登録フォーム
 */
public class LoanForm {

    @NotNull(message = "書籍を選択してください")
    private Long bookId;

    @NotNull(message = "会員を選択してください")
    private Long memberId;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
