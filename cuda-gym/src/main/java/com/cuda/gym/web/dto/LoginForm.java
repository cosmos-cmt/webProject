package com.cuda.gym.web.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class LoginForm {
@NotBlank(message = "Email is required")
@Email(message = "Please provide a valid email address")
private String email;
@NotBlank(message = "Password is required")
@Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters"
private String password;
private boolean rememberMe;
public LoginForm() {
}
public String getEmail() {
return email;
}
public void setEmail(String email) {
this.email = email != null ? email.trim() : null;
}
public String getPassword() {
return password;

}
public void setPassword(String password) {
this.password = password != null ? password.trim() : null;
}
public boolean isRememberMe() {
return rememberMe;
}
public void setRememberMe(boolean rememberMe) {
this.rememberMe = rememberMe;
}
}