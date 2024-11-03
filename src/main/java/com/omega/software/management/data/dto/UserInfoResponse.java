package com.omega.software.management.data.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@NoArgsConstructor
@Setter
public class UserInfoResponse {
	private UUID id;
	private String username;
	private String email;
	private List<String> roles;

	private String jwt;

	public UserInfoResponse(UUID id, String username, String email, List<String> roles, String jwt) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.jwt = jwt;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}
}
