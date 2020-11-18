package com.project.llol.service;

import com.project.llol.dao.BoardMapper;
import com.project.llol.dto.BoardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardMapper mapper;

    public int getBoardCount() {
        return mapper.getBoardCount();
    }

    List<BoardDTO> getBoardList(int p_start, int p_end) {
        return mapper.getBoardList(p_start, p_end);
    }

    BoardDTO getBoard(BoardDTO dto) {
        return mapper.getBoard(dto);
    }

    void increaseVisitCount(BoardDTO dto) {
        mapper.increaseVisitCount(dto);
    }

    void insertBoard(BoardDTO dto) {
        mapper.insertBoard(dto);
    }

    void updateBoard(BoardDTO dto) {
        mapper.updateBoard(dto);
    }

    void deleteBoard(BoardDTO dto) {
        mapper.deleteBoard(dto);
    }
}
