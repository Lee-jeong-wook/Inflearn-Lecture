package com.example.jpashop;

import jakarta.annotation.security.RunAs;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Test
    @Transactional
    @Rollback(false)
    public void testMember () throws  Exception{
        //given
        Member member = new Member();
        member.setUsername("memberA");
        //when
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);
        //then
        Assertions.assertEquals(findMember.getId(), (member.getId()));

    }
}