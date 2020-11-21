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

    public int getBoardCount(String searchTitle) {
        return mapper.getBoardCount(searchTitle);
    }

    public List<BoardDTO> getBoardList(String searchTitle, int p_start, int p_end) {
        return mapper.getBoardList(searchTitle, p_start, p_end);
    }

    public BoardDTO getBoard(BoardDTO dto) {
        return mapper.getBoard(dto);
    }

    public void increaseVisitCount(BoardDTO dto) {
        mapper.increaseVisitCount(dto);
    }

    public void insertBoard(BoardDTO dto) {
        mapper.insertBoard(dto);
    }

    public void updateBoard(BoardDTO dto) {
        mapper.updateBoard(dto);
    }

    public void deleteBoard(BoardDTO dto) {
        mapper.deleteBoard(dto);
    }
}
