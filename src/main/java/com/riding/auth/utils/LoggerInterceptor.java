package com.riding.auth.utils;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggerInterceptor implements HandlerInterceptor, ClientHttpRequestInterceptor {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		log.debug("Before Handler execution");
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView model)
			throws Exception {
		log.debug("Handler execution is complete");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3)
			throws Exception {
		log.debug("Request is complete");
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		logRequest(request, body);
		ClientHttpResponse response = execution.execute(request, body);
		logResponse(response);
		return response;
	}

	private void logRequest(HttpRequest request, byte[] body) throws IOException {
		log.info("Request {}: {} {}", request.getMethod(), request.getURI(), new String(body, "UTF-8"));
	}

	private void logResponse(ClientHttpResponse response) throws IOException {
		try {
			String responseStr = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
			log.debug("response: {}", response.getHeaders());
			log.info("Response {}: {} {}", response.getStatusCode(), response.getStatusText(),
					Boolean.TRUE.equals(responseStr.length() < 100) ? responseStr
							: responseStr.substring(0, 297) + "...");
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}