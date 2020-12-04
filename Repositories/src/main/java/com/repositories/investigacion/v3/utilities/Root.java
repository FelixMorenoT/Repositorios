package com.repositories.investigacion.v3.utilities;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class Root {

	@GetMapping("/")
	private RedirectView redirect() {
		return new RedirectView("/swagger-ui.html");
	}
}
