package com.project.llol.dao;

import com.project.llol.dto.BoardDTO;
import com.project.llol.dto.ReplyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReplyMapper {
    List<ReplyDTO> getReplyList(int boardnum);
    void insertReply(ReplyDTO dto);
    void updateReply(ReplyDTO dto);
    void deleteReply(int replynum);
}
