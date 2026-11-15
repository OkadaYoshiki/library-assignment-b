package com.example.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.library.entity.Book;
import com.example.library.entity.BookStatus;
import com.example.library.entity.Loan;
import com.example.library.entity.Member;
import com.example.library.exception.BusinessException;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.MemberRepository;

/**
 * LoanService の単体テスト（DB不要。Mockitoでリポジトリを差し替えて実行）
 * 実行方法： mvn test
 */
@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    LoanRepository loanRepository;
    @Mock
    BookRepository bookRepository;
    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    LoanService loanService;

    final LocalDate today = LocalDate.of(2026, 4, 1);
    Book book;
    Member member;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("はじめてのJava");
        book.setStatus(BookStatus.AVAILABLE);

        member = new Member();
        member.setId(10L);
        member.setName("青木 翔");
    }

    @Nested
    @DisplayName("貸出 lend()")
    class Lend {

        @Test
        @DisplayName("貸出できると、期限が14日後になり書籍が貸出中になる")
        void success() {
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(memberRepository.findById(10L)).thenReturn(Optional.of(member));
            when(loanRepository.countByMemberIdAndReturnDateIsNull(10L)).thenReturn(0L);
            when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

            Loan loan = loanService.lend(1L, 10L, today);

            assertThat(loan.getBook()).isSameAs(book);
            assertThat(loan.getMember()).isSameAs(member);
            assertThat(loan.getLoanDate()).isEqualTo(today);
            assertThat(loan.getDueDate()).isEqualTo(LocalDate.of(2026, 4, 15));
            assertThat(loan.getReturnDate()).isNull();
            assertThat(book.getStatus()).isEqualTo(BookStatus.LENT);
        }

        @Test
        @DisplayName("貸出中の書籍は貸し出せない")
        void alreadyLent() {
            book.setStatus(BookStatus.LENT);
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(memberRepository.findById(10L)).thenReturn(Optional.of(member));

            assertThatThrownBy(() -> loanService.lend(1L, 10L, today))
                    .isInstanceOf(BusinessException.class);
            verify(loanRepository, never()).save(any());
        }

        @Test
        @DisplayName("既に3冊借りている会員は借りられない")
        void overLimit() {
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(memberRepository.findById(10L)).thenReturn(Optional.of(member));
            when(loanRepository.countByMemberIdAndReturnDateIsNull(10L)).thenReturn(3L);

            assertThatThrownBy(() -> loanService.lend(1L, 10L, today))
                    .isInstanceOf(BusinessException.class);
            assertThat(book.getStatus()).isEqualTo(BookStatus.AVAILABLE);
            verify(loanRepository, never()).save(any());
        }

        @Test
        @DisplayName("2冊借りている会員はまだ借りられる")
        void underLimit() {
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(memberRepository.findById(10L)).thenReturn(Optional.of(member));
            when(loanRepository.countByMemberIdAndReturnDateIsNull(10L)).thenReturn(2L);
            when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

            Loan loan = loanService.lend(1L, 10L, today);
            assertThat(loan).isNotNull();
        }

        @Test
        @DisplayName("存在しない書籍は貸し出せない")
        void bookNotFound() {
            when(bookRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> loanService.lend(99L, 10L, today))
                    .isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    @DisplayName("返却 returnBook()")
    class ReturnBook {

        Loan loan;

        @BeforeEach
        void setUp() {
            book.setStatus(BookStatus.LENT);
            loan = new Loan();
            loan.setId(100L);
            loan.setBook(book);
            loan.setMember(member);
            loan.setLoanDate(today.minusDays(5));
            loan.setDueDate(today.plusDays(9));
        }

        @Test
        @DisplayName("返却すると返却日が記録され、書籍が貸出可に戻る")
        void success() {
            when(loanRepository.findById(100L)).thenReturn(Optional.of(loan));
            when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

            Loan result = loanService.returnBook(100L, today);

            assertThat(result.getReturnDate()).isEqualTo(today);
            assertThat(book.getStatus()).isEqualTo(BookStatus.AVAILABLE);
        }

        @Test
        @DisplayName("返却済みの貸出は再度返却できない")
        void alreadyReturned() {
            loan.setReturnDate(today.minusDays(1));
            when(loanRepository.findById(100L)).thenReturn(Optional.of(loan));

            assertThatThrownBy(() -> loanService.returnBook(100L, today))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        @DisplayName("存在しない貸出は返却できない")
        void notFound() {
            when(loanRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> loanService.returnBook(999L, today))
                    .isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    @DisplayName("延滞一覧 findOverdueLoans()")
    class Overdue {

        @Test
        @DisplayName("未返却かつ期限が今日より前の貸出を取得する")
        void callsRepositoryWithToday() {
            Loan overdue = new Loan();
            when(loanRepository.findByReturnDateIsNullAndDueDateBeforeOrderByDueDateAsc(today))
                    .thenReturn(List.of(overdue));

            List<Loan> result = loanService.findOverdueLoans(today);

            assertThat(result).containsExactly(overdue);
        }
    }

    @Nested
    @DisplayName("Loan エンティティ")
    class LoanEntity {

        @Test
        @DisplayName("期限当日はまだ延滞ではない。翌日から延滞")
        void overdueBoundary() {
            Loan loan = new Loan();
            loan.setDueDate(today);
            assertThat(loan.isOverdue(today)).isFalse();
            assertThat(loan.isOverdue(today.plusDays(1))).isTrue();
            assertThat(loan.overdueDays(today.plusDays(3))).isEqualTo(3);
        }
    }
}
