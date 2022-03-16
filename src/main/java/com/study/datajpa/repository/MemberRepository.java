package com.study.datajpa.repository;

import com.study.datajpa.dto.MemberDto;
import com.study.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

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

    // 성능이 안 나올 때는 count query를 분리하자
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByMemberName(String name);

    // 읽기 전용으로 사용하기 위한 QueryHints
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByMemberName(String memberName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByMemberName(String name);

}
