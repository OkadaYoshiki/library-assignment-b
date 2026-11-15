package com.example.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.library.exception.BusinessException;
import com.example.library.form.MemberForm;
import com.example.library.service.MemberService;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /** 機能⑥：会員一覧 */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", memberService.findAll());
        return "members/list";
    }

    /** 機能⑦：会員登録（入力画面） */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/form";
    }

    /** 機能⑦：会員登録（登録処理） */
    @PostMapping("/new")
    public String create(@Validated @ModelAttribute MemberForm memberForm, BindingResult result,
            Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "members/form";
        }
        try {
            memberService.register(memberForm);
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "members/form";
        }
        ra.addFlashAttribute("message", "会員を登録しました");
        return "redirect:/members";
    }
}
