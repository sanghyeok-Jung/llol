package com.project.llol.service;

import com.project.llol.dao.ReplyMapper;
import com.project.llol.dto.ReplyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {
    @Autowired
    private ReplyMapper replyMapper;

    public List<ReplyDTO> getReplyList(int boardnum) {
        return replyMapper.getReplyList(boardnum);
    }

    public void insertReply(ReplyDTO dto) {
        replyMapper.insertReply(dto);
    }

    public void updateReply(ReplyDTO dto) {
        replyMapper.updateReply(dto);
    }

    public void deleteReply(int replynum) {
        replyMapper.deleteReply(replynum);
    }
}
