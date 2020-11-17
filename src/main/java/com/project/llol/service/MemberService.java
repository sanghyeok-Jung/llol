package com.project.llol.service;

import com.project.llol.dao.MemberMapper;
import com.project.llol.dto.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    private MemberMapper mapper;

    public MemberDTO getMember(MemberDTO dto) {
        return mapper.getMember(dto);
    }

    public void insertMember(MemberDTO dto) {
        mapper.insertMember(dto);
    }

    public int checkMember(MemberDTO dto) {
        return mapper.checkMember(dto);
    }
}
