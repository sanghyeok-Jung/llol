package com.project.llol.controller;

import com.project.llol.dto.BoardDTO;
import com.project.llol.dto.MemberDTO;
import com.project.llol.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@SessionAttributes("pageNum")
public class BoardController {
    @Autowired
    private BoardService boardService;

    // 파일 업로드 경로 지정 (임시 경로와 실제 업로드 경로를 동일하게 설정해봄)
    @Value("${spring.servlet.multipart.location}")
    private String filePath;

    // 한 페이지에 노출될 게시글 개수를 상수로 지정함
    private final int POST_PER_PAGES = 20;

    @RequestMapping(value = "/boardList", method = RequestMethod.GET)
    public String boardList(Model model, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum) {
        // 전체 게시글 개수 조회
        int boardCount = boardService.getBoardCount();
        int totalPages = 1;

        List<BoardDTO> boardList = null;

        // 게시글이 1개 이상일 때에만 동작, 없으면 null 리턴
        if(boardCount >= 1) {
            // 최대 페이지 번호 설정
            totalPages = (boardCount % POST_PER_PAGES) == 0 ? (boardCount / POST_PER_PAGES) : ((boardCount / POST_PER_PAGES) + 1);

            // url을 조작해서 임의의 페이지로 이동하고자 할 때의 예외 처리
            // 요청한 페이지 번호가 전체 페이지 번호를 초과한 경우, 마지막 페이지로 변경해준다.
            if(pageNum > totalPages) {
                pageNum = totalPages;
            }

            // 게시글 번호(boardnum)를 기준으로 p_start ~ p_end 사이의 게시글 범위 지정을 위한 값 설정
            int p_end = boardCount - ((pageNum - 1) * POST_PER_PAGES);
            int p_start = p_end - POST_PER_PAGES;

            // 마지막 페이지에 POST_PER_PAGES 개수 만큼의 게시글이 없을 때 처리하기 위한 부분
            if(p_start < 1) {
                p_start = 1;
            }

            boardList = boardService.getBoardList(p_start, p_end);
        }

        // 전체 페이지 개수
        model.addAttribute("totalPages", totalPages);
        // 게시글 목록 데이터
        model.addAttribute("boardList", boardList);
        // 현재 페이지 번호
        model.addAttribute("pageNum", pageNum);

        // 화면에 보여질 최소 페이지 번호, 최대 페이지 번호 (범위를 초과하면 next, prev 버튼이 보이도록 하기 위해 설정)
        int viewFirstPage = (pageNum - 3) > 1 ? (pageNum - 3) : 1;
        int viewLastPage = (pageNum + 3) <= totalPages ? (pageNum + 3) : totalPages;
        model.addAttribute("viewFirstPage", viewFirstPage);
        model.addAttribute("viewLastPage", viewLastPage);
        return "boardList";
    }

    @RequestMapping(value = "/insertBoard", method = RequestMethod.GET)
    public String insertBoardView(HttpSession session) {
        MemberDTO user = (MemberDTO)session.getAttribute("user");
        if(user == null) {
            return "redirect:loginView";
        }
        return "insertBoard";
    }

    @RequestMapping(value = "/insertBoard", method = RequestMethod.POST)
    public String insertBoard(BoardDTO dto, HttpSession session, @RequestParam(value = "file") MultipartFile file) {
        MemberDTO user = (MemberDTO)session.getAttribute("user");
        if (user == null) {
            return "redirect:loginView";
        }

        if(!file.isEmpty()) {
            // 파일 이름.확장자 추출
            String fileName = file.getOriginalFilename();

            File uploadFile = new File(filePath + fileName);
            try {
                file.transferTo(uploadFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            dto.setBoardimage(fileName);
        } else {
            dto.setBoardimage("");
        }

        dto.setBoardwriter(user.getId());

        boardService.insertBoard(dto);
        return "redirect:boardList";
    }
}
