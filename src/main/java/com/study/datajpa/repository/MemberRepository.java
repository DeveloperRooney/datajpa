package com.study.datajpa.repository;

import com.study.datajpa.dto.MemberDto;
import com.study.datajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByMemberNameAndAgeGreaterThan(String name, int age);

    @Query("select m from Member m where m.memberName = :memberName and m.age = :age")
    List<Member> findMember(@Param("memberName") String memberName, @Param("age") int age);

    @Query("select m.memberName from Member m")
    List<String> findMemberNameList();

    @Query("select " +
            "new com.study.datajpa.dto.MemberDto(m.id, m.memberName, t.name) " +
            "from Member m " +
            "join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.memberName in :names")
    List<Member> findByMemberNames(@Param("names") Collection<String> names);

    List<Member> findListByMemberName(String name);

    Member findByMemberName(String name);

}
