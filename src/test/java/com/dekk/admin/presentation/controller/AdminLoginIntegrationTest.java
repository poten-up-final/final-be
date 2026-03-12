package com.dekk.admin.presentation.controller;

import com.dekk.admin.domain.model.Admin;
import com.dekk.admin.domain.model.AdminRole;
import com.dekk.admin.domain.repository.AdminRepository;
import com.dekk.admin.presentation.request.AdminLoginRequest;
import com.dekk.admin.presentation.response.AdminResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AdminLoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ADMIN_EMAIL = "admin@dekk.com";
    private static final String ADMIN_PASSWORD = "Password123!";

    @BeforeEach
    void setUp() {
        String encodedPassword = passwordEncoder.encode(ADMIN_PASSWORD);
        Admin admin = Admin.create(ADMIN_EMAIL, encodedPassword, AdminRole.ADMIN);
        adminRepository.save(admin);
    }

    @Test
    @DisplayName("관리자 로그인 통합 테스트 - 성공")
    void admin_login_integration_success() throws Exception {
        // given
        AdminLoginRequest request = new AdminLoginRequest(ADMIN_EMAIL, ADMIN_PASSWORD);

        // when & then
        mockMvc.perform(post("/adm/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AdminResultCode.ADMIN_LOGIN_SUCCESS.code()))
                .andExpect(cookie().exists("admin_access_token"))
                .andExpect(cookie().httpOnly("admin_access_token", true));
    }

    @Test
    @DisplayName("관리자 로그인 통합 테스트 - 비밀번호 불일치")
    void admin_login_integration_invalid_password() throws Exception {
        // given
        AdminLoginRequest request = new AdminLoginRequest(ADMIN_EMAIL, "WrongPassword123!");

        // when & then
        mockMvc.perform(post("/adm/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("EAD40101"));
    }
}
