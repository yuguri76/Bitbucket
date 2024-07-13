package com.sparta.bitbucket.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 웹 페이지 라우팅을 처리하는 컨트롤러
 * 각 페이지에 대한 요청을 처리합니다.
 */
@Controller
public class PageController {

	/**
	 * 로그인 페이지로 이동
	 * 
	 * @return "Login" 문자열을 이용하여 Login.html 템플릿을 렌더링
	 */
	@RequestMapping("/login")
	public String loginPage() {
		return "Login";
	}

	/**
	 * 메인 페이지로 이동
	 *
	 * @return "Main" 문자열을 이용하여 Main.html 템플릿을 렌더링
	 */
	@RequestMapping("/main")
	public String mainPage() {
		return "Main";
	}

	/**
	 * 회원가입 페이지로 이동
	 *
	 * @return "SignUp" 문자열을 이용하여 SignUp.html 템플릿을 렌더링
	 */
	@RequestMapping("/signup")
	public String signupPage() {
		return "SignUp";
	}

	/**
	 * Board 만드는 페이지로 이동
	 *
	 * @return "create_board" 문자열을 이용하여 create_board.html 템플릿 렌더링
	 */
	@RequestMapping("/create_board")
	public String createBoardPage(){
		return "create_board";
	}

}
