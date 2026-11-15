package com.example.library.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.library.exception.BusinessException;
import com.example.library.form.LoanForm;
import com.example.library.service.BookService;
import com.example.library.service.LoanService;
import com.example.library.service.MemberService;

@Controller
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;
    private final MemberService memberService;

    public LoanController(LoanService loanService, BookService bookService, MemberService memberService) {
        this.loanService = loanService;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    /** 貸出中一覧 */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("loans", loanService.findActiveLoans());
        model.addAttribute("today", LocalDate.now());
        return "loans/list";
    }

    /** 機能⑧：貸出（入力画面） */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("loanForm", new LoanForm());
        addSelectOptions(model);
        return "loans/form";
    }

    /** 機能⑧：貸出（登録処理） */
    @PostMapping("/new")
    public String create(@Validated @ModelAttribute LoanForm loanForm, BindingResult result,
            Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            addSelectOptions(model);
            return "loans/form";
        }
        try {
            loanService.lend(loanForm.getBookId(), loanForm.getMemberId(), LocalDate.now());
        } catch (BusinessException e) {
            addSelectOptions(model);
            model.addAttribute("errorMessage", e.getMessage());
            return "loans/form";
        }
        ra.addFlashAttribute("message", "貸出を登録しました");
        return "redirect:/loans";
    }

    /** 機能⑨：返却 */
    @PostMapping("/{id}/return")
    public String returnBook(@PathVariable Long id, RedirectAttributes ra) {
        try {
            loanService.returnBook(id, LocalDate.now());
            ra.addFlashAttribute("message", "返却を受け付けました");
        } catch (BusinessException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/loans";
    }

    // TODO 課題B-3：延滞一覧画面を追加する
    //   GET /loans/overdue
    //   ・loanService.findOverdueLoans(今日の日付) の結果を "loans"、今日の日付を "today" として Model に入れる
    //   ・loans/overdue.html を表示する（テンプレートも自分で作成）

    private void addSelectOptions(Model model) {
        model.addAttribute("books", bookService.findAvailable());
        model.addAttribute("members", memberService.findAll());
    }
}
