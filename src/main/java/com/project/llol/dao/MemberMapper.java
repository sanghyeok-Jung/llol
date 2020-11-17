package com.project.llol.dao;

import com.project.llol.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MemberMapper {
    public MemberDTO getMember(MemberDTO dto);
    public void insertMember(MemberDTO dto);
    public int checkMember(MemberDTO dto);
    public void updateMember(MemberDTO dto);
}
