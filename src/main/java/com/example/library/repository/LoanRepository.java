package com.example.library.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    /** 貸出中（未返却）の一覧を返却期限の近い順で取得 */
    List<Loan> findByReturnDateIsNullOrderByDueDateAsc();

    /** 延滞中（未返却かつ返却期限が指定日より前）の一覧を取得  ※課題B-3で使います */
    List<Loan> findByReturnDateIsNullAndDueDateBeforeOrderByDueDateAsc(LocalDate date);

    /** 会員が現在借りている冊数 */
    long countByMemberIdAndReturnDateIsNull(Long memberId);

    /** 書籍に貸出履歴があるか */
    boolean existsByBookId(Long bookId);
}
