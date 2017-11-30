package de.terrestris.shogun2.web;

import de.terrestris.shogun2.service.HttpProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO: add documentation
 *
 * @author Andre Henn
 */
@Controller
public class HttpProxyController {

	@Autowired
	@Qualifier("httpProxyService")
	private HttpProxyService proxyService;

	@RequestMapping("/proxy.action")
	public @ResponseBody ResponseEntity doProxy(@RequestParam String url, HttpServletRequest request){
		return proxyService.doProxy(url, request);
	}
}
