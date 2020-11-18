package com.project.llol.dao;

import com.project.llol.dto.BoardDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BoardMapper {
    int getBoardCount();
    List<BoardDTO> getBoardList(int p_start, int p_end);
    BoardDTO getBoard(BoardDTO dto);
    void increaseVisitCount(BoardDTO dto);
    void insertBoard(BoardDTO dto);
    void updateBoard(BoardDTO dto);
    void deleteBoard(BoardDTO dto);
}
