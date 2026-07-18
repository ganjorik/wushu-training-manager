package com.wushu.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public String handleBusinessException(
			BusinessException e,
			Model model) {

		model.addAttribute(
				"error",
				e.getMessage()
		);

		return "error";
	}
}
