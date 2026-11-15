package com.example.library.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.library.entity.Book;
import com.example.library.entity.BookStatus;
import com.example.library.entity.Loan;
import com.example.library.entity.Member;
import com.example.library.exception.BusinessException;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class LoanService {

    /** 貸出期間（日） */
    public static final int LOAN_DAYS = 14;
    /** 1人が同時に借りられる上限冊数 */
    public static final int MAX_LOANS_PER_MEMBER = 3;

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository,
            MemberRepository memberRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    /** 貸出中の一覧 */
    public List<Loan> findActiveLoans() {
        return loanRepository.findByReturnDateIsNullOrderByDueDateAsc();
    }

    /**
     * 機能⑧：貸出
     *
     * TODO 課題B-1：貸出処理を実装する
     *   業務ルール
     *   ・書籍が存在しなければ BusinessException（「書籍が見つかりません」）
     *   ・会員が存在しなければ BusinessException（「会員が見つかりません」）
     *   ・書籍が貸出中なら BusinessException（「（タイトル）」は貸出中です）
     *   ・会員の貸出中冊数が MAX_LOANS_PER_MEMBER 以上なら BusinessException
     *       （○○さんは既に3冊借りているため、これ以上借りられません）
     *   処理
     *   ・Loan を作成：book, member, loanDate=today, dueDate=today の LOAN_DAYS 日後
     *   ・書籍の状態を LENT にして保存
     *   ・Loan を保存して返す
     *   ヒント：findById(...).orElseThrow(() -> new BusinessException("...")) が便利です
     */
    @Transactional
    public Loan lend(Long bookId, Long memberId, LocalDate today) {
        throw new UnsupportedOperationException("課題B-1：未実装です");
    }

    /**
     * 機能⑨：返却
     *
     * TODO 課題B-2：返却処理を実装する
     *   ・貸出記録が存在しなければ BusinessException（「貸出記録が見つかりません」）
     *   ・既に返却済みなら BusinessException（「この本は既に返却済みです」）
     *   ・returnDate に today をセット
     *   ・書籍の状態を AVAILABLE に戻して保存
     *   ・Loan を保存して返す
     */
    @Transactional
    public Loan returnBook(Long loanId, LocalDate today) {
        throw new UnsupportedOperationException("課題B-2：未実装です");
    }

    /**
     * 機能⑩：延滞一覧（返却期限を過ぎて未返却のもの）
     *
     * TODO 課題B-3：LoanRepository#findByReturnDateIsNullAndDueDateBeforeOrderByDueDateAsc を使って実装する
     */
    public List<Loan> findOverdueLoans(LocalDate today) {
        return List.of();
    }
}
