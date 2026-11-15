package com.example.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.library.exception.BusinessException;
import com.example.library.form.BookForm;
import com.example.library.service.BookService;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /** 機能①②：書籍一覧・検索 */
    @GetMapping
    public String list(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("books", bookService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "books/list";
    }

    /** 機能③：書籍登録（入力画面） */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("bookForm", new BookForm());
        model.addAttribute("mode", "new");
        return "books/form";
    }

    /** 機能③：書籍登録（登録処理） */
    @PostMapping("/new")
    public String create(@Validated @ModelAttribute BookForm bookForm, BindingResult result,
            Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "new");
            return "books/form";
        }
        try {
            bookService.register(bookForm);
        } catch (BusinessException e) {
            model.addAttribute("mode", "new");
            model.addAttribute("errorMessage", e.getMessage());
            return "books/form";
        }
        ra.addFlashAttribute("message", "書籍を登録しました");
        return "redirect:/books";
    }

    /** 機能④：書籍編集（入力画面） */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("bookForm", bookService.toForm(bookService.findById(id)));
        model.addAttribute("bookId", id);
        model.addAttribute("mode", "edit");
        return "books/form";
    }

    /** 機能④：書籍編集（更新処理） */
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Validated @ModelAttribute BookForm bookForm,
            BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("bookId", id);
            model.addAttribute("mode", "edit");
            return "books/form";
        }
        try {
            bookService.update(id, bookForm);
        } catch (BusinessException e) {
            model.addAttribute("bookId", id);
            model.addAttribute("mode", "edit");
            model.addAttribute("errorMessage", e.getMessage());
            return "books/form";
        }
        ra.addFlashAttribute("message", "書籍を更新しました");
        return "redirect:/books";
    }

    /** 機能⑤：書籍削除 */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            bookService.delete(id);
            ra.addFlashAttribute("message", "書籍を削除しました");
        } catch (BusinessException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/books";
    }
}
