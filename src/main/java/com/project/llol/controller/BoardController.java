package com.project.llol.controller;

import com.project.llol.dto.BoardDTO;
import com.project.llol.dto.MemberDTO;
import com.project.llol.dto.ReplyDTO;
import com.project.llol.service.BoardService;
import com.project.llol.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private ReplyService replyService;

    // 파일 업로드 경로 지정 (임시 경로와 실제 업로드 경로를 동일하게 설정)
    @Value("${spring.servlet.multipart.location}")
    private String filePath;

    // 한 페이지에 노출될 게시글 개수를 상수로 지정함
    private final int POST_PER_PAGES = 10;

    @RequestMapping(value = "/boardList", method = RequestMethod.GET)
    public String boardList(Model model, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum, @RequestParam(value = "title", defaultValue = "") String title) {
        // 제목 검색 기능 지원을 위해 쿼리문에 대입할 문자열 가공
        String searchTitle = "%" + title + "%";

        // 전체 게시글 개수 조회
        int boardCount = boardService.getBoardCount(searchTitle);
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
            int p_start = p_end - POST_PER_PAGES + 1;

            // 마지막 페이지에 POST_PER_PAGES 개수 만큼의 게시글이 없을 때 처리하기 위한 부분
            if(p_start < 1) {
                p_start = 1;
            }

            boardList = boardService.getBoardList(searchTitle, p_start, p_end);
        }

        // 전체 페이지 개수
        model.addAttribute("totalPages", totalPages);
        // 게시글 목록 데이터
        model.addAttribute("boardList", boardList);
        // 현재 페이지 번호
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("title", title);

        // 화면에 보여질 최소 페이지 번호, 최대 페이지 번호 (범위를 초과하면 next, prev 버튼이 보이도록 하기 위해 설정)
        int viewFirstPage = (pageNum - 3) > 1 ? (pageNum - 3) : 1;
        int viewLastPage = (pageNum + 3) <= totalPages ? (pageNum + 3) : totalPages;
        model.addAttribute("viewFirstPage", viewFirstPage);
        model.addAttribute("viewLastPage", viewLastPage);
        return "boardList";
    }

    @RequestMapping(value = "/insertBoard", method = RequestMethod.GET)
    public String insertBoardView(HttpSession session, Model model, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum, @RequestParam(value = "title", defaultValue = "") String title) {
        // 로그인 상태가 아니라면 로그인 화면으로 이동시킨다.
        MemberDTO user = (MemberDTO)session.getAttribute("user");
        if(user == null) {
            return "redirect:loginView";
        }

        model.addAttribute("pageNum", pageNum);
        model.addAttribute("title", title);
        return "insertBoard";
    }

    @RequestMapping(value = "/insertBoard", method = RequestMethod.POST)
    public String insertBoard(BoardDTO dto, HttpSession session, @RequestParam(value = "file") MultipartFile file) {
        // 로그인 상태가 아니라면 로그인 화면으로 이동시킨다.
        MemberDTO user = (MemberDTO)session.getAttribute("user");
        if (user == null) {
            return "redirect:loginView";
        }

        // 파일이 비어있는지 확인함 (비어있지 않으면 정상적으로 동작함)
        if(!file.isEmpty()) {
            // 파일 이름.확장자 추출(경로를 제거하고 정확히 파일 이름과 확장자만 추출함)
            String fileName = file.getOriginalFilename();

            // 업로드될 파일 설정 (개인적으로 지정한 경로 + 파일의 오리지널이름)
            File uploadFile = new File(filePath + fileName);
            try {
                // 페이지에서 전달받은 파일을 다음과 같은 정보로 업로드 처리 한다.
                file.transferTo(uploadFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // DB에는 파일의 오리지널이름만 저장해준다.
            dto.setBoardimage(fileName);
        } else {
            // 파일이 비어있으면 값을 공백으로 처리 한다.
            dto.setBoardimage("");
        }

        // 작성자 아이디는 현재 세션에 저장된 사용자 아이디로 설정한다.
        dto.setBoardwriter(user.getId());

        // 일반 유튜브 링크 예제 : https://www.youtube.com/watch?v=NBhSf_CVTJ8
        // iframe 폼 url을 사용하기 위해, 마지막 속성값을 추출해서 db에 저장한다. (NBhSf_CVTJ8)
        dto.setBoardyoutube(dto.getBoardyoutube().replace("https://www.youtube.com/watch?v=", ""));

        boardService.insertBoard(dto);
        return "redirect:boardList";
    }

    @RequestMapping(value = "/getBoard", method = RequestMethod.GET)
    public String getBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model, @RequestParam(value = "boardnum") int boardnum, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum, @RequestParam(value = "title", defaultValue = "") String title) {
        MemberDTO user = (MemberDTO)session.getAttribute("user");
        if(user == null) {
            return "redirect:loginView";
        }

        // 전체 쿠키 목록을 가져옴
        Cookie[] cookies = request.getCookies();
        // 동일한 값을 가지는 쿠키가 있는지 확인하기 위한 체크용
        boolean checkCookie = false;
        if(cookies != null) {
            for(Cookie c : cookies) {
                if(c.getValue().equals(Integer.toString(boardnum))) {
                    checkCookie = true;
                }
            }
            // 쿠키 전체를 조회했는데도 일치하는 값이 없었으면, 회원 데이터를 쿠키에 저장
            if(!checkCookie) {
                Cookie cookie = new Cookie("boardnum", Integer.toString(boardnum));
                cookie.setMaxAge(60*60*24*1);
                response.addCookie(cookie);
            }
        } else {
            // 쿠키가 아예 없어도 회원데이터를 쿠키에 저장
            Cookie cookie = new Cookie("id", user.getId());
            cookie.setMaxAge(60*60*24*1);
            response.addCookie(cookie);
        }

        BoardDTO board = new BoardDTO();
        board.setBoardnum(boardnum);
        // 쿠키 체크할 때 해당 값이 없었다면
        // 조회 수 증가를 실행한다.
        if(checkCookie == false) {
            boardService.increaseVisitCount(board);
        }
        // 조회수가 증가한 게시글 데이터를 가져옴
        board = boardService.getBoard(board);

        List<ReplyDTO> replyList = replyService.getReplyList(boardnum);

        model.addAttribute("board", board);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("title", title);
        model.addAttribute("replyList", replyList);
        return "getBoard";
    }

    @RequestMapping(value = "/deleteBoard", method = RequestMethod.POST)
    public String deleteBoard(HttpSession session, @RequestParam(value = "boardnum") int boardnum, @RequestParam(value = "boardwriter") String boardwriter) {
        MemberDTO user = (MemberDTO)session.getAttribute("user");
        // 로그인 상태가 아니면 로그인 화면으로 이동
        if(user == null) {
            return "redirect:loginView";
        }
        // 게시물 작성자랑 로그인중인 계정 id랑 일치하지 않으면 로그인 화면으로 이동
        if(!user.getId().equals(boardwriter)) {
            return "redirect:loginView";
        }

        BoardDTO board = new BoardDTO();
        board.setBoardnum(boardnum);
        boardService.deleteBoard(board);

        return "redirect:boardList";
    }

    @RequestMapping(value = "/updateBoardView", method = RequestMethod.POST)
    public String updateBoardView(HttpSession session, Model model, @RequestParam(value = "boardnum") int boardnum, @RequestParam(value = "boardwriter") String boardwriter, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum, @RequestParam(value = "title", defaultValue = "") String title) {
        MemberDTO user = (MemberDTO)session.getAttribute("user");
        if(user == null) {
            return "redirect:loginView";
        }
        if(!user.getId().equals(boardwriter)) {
            return "redirect:loginView";
        }

        BoardDTO board = new BoardDTO();
        board.setBoardnum(boardnum);
        board = boardService.getBoard(board);

        model.addAttribute("board", board);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("title", title);
        return "updateBoard";
    }

    @RequestMapping(value = "/updateBoard", method = RequestMethod.POST)
    public String updateBoard(HttpSession session, Model model, BoardDTO board, @RequestParam(value = "file", required = false, defaultValue = "") MultipartFile file, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum, @RequestParam(value = "title", defaultValue = "") String title) {
        MemberDTO user = (MemberDTO)session.getAttribute("user");
        if(user == null) {
            return "redirect:loginView";
        }
        if(!user.getId().equals(board.getBoardwriter())) {
            return "redirect:loginView";
        }

        // 파일이 비어있는지 확인함 (비어있지 않으면 신규 이미지가 업로드 됨)
        if(!file.isEmpty()) {
            // 파일 이름.확장자 추출(경로를 제거하고 정확히 파일 이름과 확장자만 추출함)
            String fileName = file.getOriginalFilename();

            // 업로드될 파일 설정 (개인적으로 지정한 경로 + 파일의 오리지널이름)
            File uploadFile = new File(filePath + fileName);
            try {
                // 페이지에서 전달받은 파일을 다음과 같은 정보로 업로드 처리 한다.
                file.transferTo(uploadFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // DB에는 파일의 오리지널이름만 저장해준다.
            board.setBoardimage(fileName);
        } else if(board.getBoardimage() == null){
            board.setBoardimage("");
        }

        if(!board.getBoardyoutube().equals("")) {
            board.setBoardyoutube(board.getBoardyoutube().replace("https://www.youtube.com/watch?v=", ""));
        }

        boardService.updateBoard(board);

        return "redirect:getBoard?boardnum=" + board.getBoardnum() + "&pageNum=" + pageNum + "&title=" + title;
    }
}
