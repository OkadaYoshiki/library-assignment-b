package com.example.library.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.library.entity.Member;
import com.example.library.exception.BusinessException;
import com.example.library.form.MemberForm;
import com.example.library.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findAll() {
        return memberRepository.findAllByOrderByIdAsc();
    }

    /** 機能⑦：会員登録。メールアドレスの重複は不可 */
    @Transactional
    public Member register(MemberForm form) {
        if (memberRepository.existsByEmail(form.getEmail())) {
            throw new BusinessException("このメールアドレスは既に登録されています");
        }
        Member member = new Member();
        member.setName(form.getName());
        member.setEmail(form.getEmail());
        return memberRepository.save(member);
    }
}
