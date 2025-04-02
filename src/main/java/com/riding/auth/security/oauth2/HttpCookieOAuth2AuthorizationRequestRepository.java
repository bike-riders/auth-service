package com.riding.auth.security.oauth2;

import java.io.IOException;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.riding.auth.utils.CookieUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpCookieOAuth2AuthorizationRequestRepository
		implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
	public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
	public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
	private static final int cookieExpireSeconds = 180;

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		OAuth2AuthorizationRequest oAuth2AuthorizationRequest = CookieUtils
				.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME).map(cookie -> {
					try {
						return CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class);
					} catch (ClassNotFoundException | IOException e) {

						e.printStackTrace();
						return null;
					}

				}).orElse(null);
		log.info("AuthRequestURI:{}, AuthorizationUri: {}, RedirectUri: {}",
				oAuth2AuthorizationRequest.getAuthorizationRequestUri(),
				oAuth2AuthorizationRequest.getAuthorizationUri(), oAuth2AuthorizationRequest.getRedirectUri());
		return oAuth2AuthorizationRequest;
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
			HttpServletResponse response) {
		if (authorizationRequest == null) {
			CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
			CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
			return;
		}

		try {
			CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
					CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
		if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
			CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
		}
	}

	public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
		CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
			HttpServletResponse response) {
		return this.loadAuthorizationRequest(request);
	}
}
