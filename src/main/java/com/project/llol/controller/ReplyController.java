package com.project.llol.controller;

import com.project.llol.dto.MemberDTO;
import com.project.llol.dto.ReplyDTO;
import com.project.llol.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class ReplyController {
    @Autowired
    private ReplyService replyService;

    @RequestMapping(value = "/insertReply", method = RequestMethod.POST)
    public String insertReply(HttpSession session, ReplyDTO reply, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum, @RequestParam(value = "title", required = false, defaultValue = "") String title) {
        MemberDTO user = (MemberDTO)session.getAttribute("user");
        if(user == null) {
            return "redirect:loginView";
        }

        // 작성자 이름은 세션에서 가져옴
        reply.setReplywriter(user.getId());

        replyService.insertReply(reply);
        return "redirect:getBoard?boardnum=" + reply.getBoardnum() + "&pageNum=" + pageNum + "&title=" + title;
    }

    @RequestMapping(value = "/deleteReply", method = RequestMethod.POST)
    public String deleteReply(@RequestParam(value = "boardnum") int boardnum, @RequestParam(value = "replynum") int replynum, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum, @RequestParam(value = "title", required = false, defaultValue = "") String title) {
        replyService.deleteReply(replynum);
        return "redirect:getBoard?boardnum=" + boardnum + "&pageNum=" + pageNum + "&title=" + title;
    }

    @RequestMapping(value = "/updateReply", method = RequestMethod.POST)
    public String updateReply(@RequestParam(value = "boardnum") int boardnum,
                              @RequestParam(value = "replynum") int replynum,
                              @RequestParam(value = "replycontent") String replycontent,
                              @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                              @RequestParam(value = "title", required = false, defaultValue = "") String title) {
        ReplyDTO reply = new ReplyDTO();
        reply.setReplynum(replynum);
        reply.setReplycontent(replycontent);
        replyService.updateReply(reply);

        return "redirect:getBoard?boardnum=" + boardnum + "&pageNum=" + pageNum + "&title=" + title;
    }
}
