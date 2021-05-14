package com.jwt.authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {
	
	@RequestMapping("/welcome")
	public String welcome() {
		String text = "Esta página é privida";
		text += "está página não poderá ser acessaada pro usuários não autorizados.";
		return text;
	}
	
	@RequestMapping("/getusers")
	public String getUser() {

		return "{\name\":\"juliano\"}";
	}
		
}
