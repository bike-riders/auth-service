package com.riding.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.riding.auth.model.enums.AuthProvider;
import com.riding.auth.model.enums.UserRoles;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpRequest {
	private String deviceId;
	private String firstName;
	private String lastName;
	private String fullName;
	private String user;
	@Email
	private String email;
	private String mobile;
	private String password;
	@Enumerated(EnumType.STRING)
	private UserRoles userRole;
	@Enumerated(EnumType.STRING)
	private AuthProvider provider;
	@JsonIgnore
	private String imageUrl;
	private String fcmId;
	private Integer status;
}
