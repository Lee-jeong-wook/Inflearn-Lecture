package com.example.jpashop.service;

import com.example.jpashop.domain.Member;
import com.example.jpashop.repository.MemberRepository;
//import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor //set constructor with final argumanet
public class MemberService {
    //field injection
//    @Autowired
    private final MemberRepository memberRepository;
//    setter injection
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }
//    @Autowired
//    public MemberService(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }

    //회원 가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //Exception
        List<Member> findbyName = memberRepository.findbyName(member.getName());
        if(!findbyName.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원");
        }
    }

//    @Transactional(readOnly = true)
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
//    @Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);

    }
}
