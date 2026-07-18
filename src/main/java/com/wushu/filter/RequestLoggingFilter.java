package com.wushu.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {

		String method = request.getMethod();
		String uri = request.getRequestURI();
		String clientIp = request.getRemoteAddr();

		log.info(
				"Incoming {} {} from {}",
				method,
				uri,
				clientIp
		);

		long start = System.currentTimeMillis();

		filterChain.doFilter(request, response);

		long executionTime = System.currentTimeMillis() - start;

		log.info(
				"Completed {} {} -> {} ({} ms)",
				method,
				uri,
				response.getStatus(),
				executionTime
		);
	}
}