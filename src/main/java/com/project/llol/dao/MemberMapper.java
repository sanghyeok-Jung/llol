package com.project.llol.dao;

import com.project.llol.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MemberMapper {
    MemberDTO getMember(MemberDTO dto);
    void insertMember(MemberDTO dto);
    int checkMember(MemberDTO dto);
    void updateMember(MemberDTO dto);
}
