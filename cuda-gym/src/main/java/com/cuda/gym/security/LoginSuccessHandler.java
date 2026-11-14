package com.cuda.gym.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_MEMBER = "ROLE_MEMBER";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws ServletException, IOException {

        String redirectUrl = determineTargetUrl(request, authentication);

              getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

        protected String determineTargetUrl(HttpServletRequest request,
                                        Authentication authentication) {

        Object principal = authentication.getPrincipal();

                if (principal instanceof CustomUserDetails userDetails) {
            if (userDetails.hasRole(ROLE_ADMIN)) {
                return request.getContextPath() + "/admin/dashboard";
            }
            if (userDetails.hasRole(ROLE_MEMBER)) {
                return request.getContextPath() + "/member/profile";
            }
        }

                boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> ROLE_ADMIN.equals(a.getAuthority()));

        if (isAdmin) {
            return request.getContextPath() + "/admin/dashboard";
        }

        boolean isMember = authentication.getAuthorities().stream()
                .anyMatch(a -> ROLE_MEMBER.equals(a.getAuthority()));

        if (isMember) {
            return request.getContextPath() + "/member/profile";
        }

               return request.getContextPath() + "/";
    }
}