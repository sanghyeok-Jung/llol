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
    public String boardList(Model model, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum) {
        int p_end = boardService.getBoardCount() - ((pageNum - 1) * POST_PER_PAGES);
        int p_start = p_end - POST_PER_PAGES;

        List<BoardDTO> boardList = boardService.getBoardList(p_start, p_end);
        model.addAttribute("boardList", boardList);
        return "boardList";
    }
}
