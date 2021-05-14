package com.jwt.authentication.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwt.authentication.helper.JwtUtil;
import com.jwt.authentication.service.CustomUserDetailsService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//Pega o jwt, usa o Bearer, valida
		
		String requestTokenHeader = request.getHeader("Authorizaation");
		String username = null;
		String jwtToken = null;
		if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);			
			
			try {
				
				username = jwtUtil.getUsernameFromToken(jwtToken);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
			
			//Segurança
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =   new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				
			} else {
				
				System.out.println("O Token não foi validado.");
				
				
			}
			
		}
		
		filterChain.doFilter(request, response);
		
	}	

}
