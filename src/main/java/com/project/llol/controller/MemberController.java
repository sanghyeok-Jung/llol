package com.project.llol.controller;

import com.project.llol.dto.MemberDTO;
import com.project.llol.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes("user")
public class MemberController {
    @Autowired
    private MemberService memberService;

    // loginView
    // 세션에 사용자 정보가 있으면 main.html로 이동한다.
    // 세션에 사용자 정보가 없으면 login.html로 이동한다.
    @RequestMapping(value = "/loginView", method = RequestMethod.GET)
    public String loginView(HttpSession session) {
        MemberDTO user = (MemberDTO)session.getAttribute("user");
        if(user != null) {
            return "main";
        }
        return "login";
    }

    // login
    // 세션에 사용자 정보가 없으면 지금 요청된 사용자 정보를 세션에 저장한다.
    // 세션에 사용자 정보가 있으면 어떤 처리도 하지 않는다.
    // 처리가 끝나면 main.html로 redirect한다.
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, MemberDTO dto) {
        if(session.getAttribute("user") == null) {
            MemberDTO user = memberService.getMember(dto);
            session.setAttribute("user", user);
        }
        return "redirect:main";
    }

    // logout
    // @SessionAttributes로 저장된 객체들을 초기화 시킨다.
    // 초기화 후 main.html로 redirect한다.
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "redirect:main";
    }

    // signUpView
    // signUp.html 페이지로 이동한다.
    @RequestMapping(value = "/signUpView", method = RequestMethod.GET)
    public String signUpView() {
        return "signUp";
    }

    // signUp
    // 요청받은 폼에 입력된 데이터를 DB에 insert한다.
    // 처리가 완료되면 main.html로 redirect한다.
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public String signUp(MemberDTO dto) {
        memberService.insertMember(dto);
        return "redirect:main";
    }

    // checkMember
    // id값을 기준으로 동일한 아이디를 사용중인 계정이 있는지 체크한다.
    // SELECT COUNT(*) FROM member WHERE id = #{id}
    // 1개 이상 있는 경우, 현재 해당 아이디가 존재한다는 것을 의미함.
    // 사용 영역 : 로그인, 회원 가입
    // ajax 통신으로 전달해주기 위해 responsebody를 사용한다.
    @RequestMapping(value = "/checkMember", method = RequestMethod.GET)
    @ResponseBody
    public int checkMember(MemberDTO dto) {
        return memberService.checkMember(dto);
    }

    // getMemberInfo
    // ajax 통신으로 전달해주기 위해 responsebody를 사용한다.
    // 요청받은 id의 비밀번호를 반환해준다.
    @RequestMapping(value = "/getMemberInfo", method = RequestMethod.POST)
    @ResponseBody
    public String getMemberInfo(MemberDTO dto) {
        String password = memberService.getMember(dto).getPassword();
        return password;
    }

    // updateMemberView
    // updateMember.html로 이동하기 위한 처리
    // 세션에 저장된 유저 정보가 없으면 로그인 페이지로 이동
    // 세션에 저장된 유저 정보가 있으면 모델에 객체를 저장해서 회원정보 수정 페이지로 전달
    @RequestMapping(value = "/updateMember", method = RequestMethod.GET)
    public String updateMemberView(HttpSession session, Model model) {
        MemberDTO user = (MemberDTO)session.getAttribute("user");
        if(user == null) {
            return "login";
        }

        model.addAttribute("user", user);
        return "updateMember";
    }

    // updateMember
    // 회원 정보 수정 페이지에서 입력된 데이터를 DB에 반영함
    @RequestMapping(value = "/updateMember", method = RequestMethod.POST)
    public String updateMember(MemberDTO dto, Model model, @RequestParam(value = "origin_password", required = false) String origin_password) {
        if(dto.getPassword().equals("")) {
            dto.setPassword(origin_password);
        }
        memberService.updateMember(dto);

        model.addAttribute("user", dto);
        return "redirect:updateMember";
    }
}
