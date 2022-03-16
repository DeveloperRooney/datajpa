package com.study.datajpa.repository;

import com.study.datajpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    private MemberRepository repository;

    @Test
    public void testMember() {
        Member member = new Member("hancoding");
        Member savedMember = repository.save(member);

        Member findMember = repository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

        assertThat(findMember.getId())
                .isEqualTo(member.getId());

        assertThat(findMember.getMemberName())
                .isEqualTo(member.getMemberName());

    }
}