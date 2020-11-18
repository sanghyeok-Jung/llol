package com.project.llol.controller;

import com.project.llol.dto.BoardDTO;
import com.project.llol.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    // 한 페이지에 노출될 게시글 개수를 상수로 지정함
    private final int POST_PER_PAGES = 20;

    @RequestMapping(value = "/boardList", method = RequestMethod.GET)
    public String boardList(Model model, @RequestParam(value = "pageNum", required = false) int pageNum) {
        // 전체 게시글 개수 조회
        int boardCount = boardService.getBoardCount();

        // 최대 페이지 번호 설정
        int pages = (boardCount % POST_PER_PAGES) == 0 ? (boardCount / POST_PER_PAGES) : ((boardCount / POST_PER_PAGES) + 1);

        // 게시글 번호(boardnum)를 기준으로 p_start ~ p_end 사이의 게시글 범위 지정을 위한 값 설정
        int p_end = boardCount - ((pageNum - 1) * POST_PER_PAGES);
        int p_start = p_end - POST_PER_PAGES;

        // 전체 게시물 개수가 POST_PER_PAGES 보다 적을 때 처리하기 위한 부분
        if(p_end < POST_PER_PAGES) {
            p_end = POST_PER_PAGES;
        }
        // 마지막 페이지에 POST_PER_PAGES 개수 만큼의 게시글이 없을 때 처리하기 위한 부분
        if(p_start < 1) {
            p_start = 1;
        }

        List<BoardDTO> boardList = boardService.getBoardList(p_start, p_end);

        model.addAttribute("pages", pages);
        model.addAttribute("boardList", boardList);
        return "boardList";
    }
}
