    package com.dao.nbti.common.config;

    import com.dao.nbti.common.jwt.JwtAuthenticationFilter;
    import com.dao.nbti.common.jwt.JwtTokenProvider;
    import com.dao.nbti.common.jwt.RestAccessDeniedHandler;
    import com.dao.nbti.common.jwt.RestAuthenticationEntryPoint;
    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
    import org.springframework.web.filter.CorsFilter;

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity // @PreAuthorize, @PostAuthorize 사용을 위해 설정
    @RequiredArgsConstructor
    public class SecurityConfig {
        private final JwtTokenProvider jwtTokenProvider;
        private final UserDetailsService userDetailsService;
        private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
        private final RestAccessDeniedHandler restAccessDeniedHandler;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .exceptionHandling(exception ->
                            exception.authenticationEntryPoint(restAuthenticationEntryPoint) // 인증 실패
                                    .accessDeniedHandler(restAccessDeniedHandler)) // 인가 실패
                    .authorizeHttpRequests(
                            auth -> {
                                permitAllEndpoints(auth);
                                userEndpoints(auth);
                                adminEndpoints(auth);
                                commonEndpoints(auth);

                                // 이 외의 요청은 인증 필요
                                auth.anyRequest().authenticated();
                            }
                    )
                    .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

            /* CORS 설정 */
            http.cors(cors -> cors
                    .configurationSource(corsConfigurationSource()));
            return http.build();
        }

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() {
            return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
        }

        @Bean
        public CorsFilter corsFilter() {
            return new CorsFilter(corsConfigurationSource());
        }

        @Bean
        public UrlBasedCorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration config = new CorsConfiguration();
            config.addAllowedOrigin("http://localhost:5173"); // 허용할 도메인
            config.addAllowedHeader("*"); // 모든 헤더 허용
            config.addAllowedMethod("*"); // 모든 HTTP 메소드 허용
            config.setAllowCredentials(true);// 자격 증명(쿠키 등) 허용
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);// 모든 경로에 대해 설정
            return source;
        }

        // 인증 없이 접근 허용
        private void permitAllEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
            auths.requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-resources/**",
                    "/v3/api-docs/**",
                    "/user/signup",
                    "/user/id-duplicate",
                    "/user/find-password",
                    "/user/reset-password",
                    "/test/problems",
                    "/test-result/now/**",
                    "/test-result",
                    "/study/category",
                    "/actuator/**"
            ).permitAll();

            // 로그인 및 토큰 관련 (모두 허용)
            auths.requestMatchers(
                    "/user/login",
                    "/user/refresh"
            ).permitAll();
        }

        // 회원(로그인 사용자)만 접근 가능
        private void userEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
            auths.requestMatchers(
                    "/user/logout",
                    "/user/withdraw",
                    "/user/info",
                    "/user/points",

                    "/test-result/list",
                    "/test-result/{testResultId}",
                    "/test-result/{testResultId}/my-page",

                    "/mypage/studies",
                    "/mypage/studies/{studyId}",

                    "/mypage/objections",
                    "/mypage/objections/{objectionId}",

                    "/study/problem",
                    "/study/submit"
            ).hasAuthority("USER");
        }

        // 관리자 전용
        private void adminEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
            auths.requestMatchers(
                    "/user/list",
                    "/test-result/list/admin",
                    "/test-result/{testResultId}/admin",
                    "/admin/study",

                    "/admin/problems/list",
                    "/admin/problems",
                    "/admin/problems/{problemId}",
                    "/admin/categories",

                    "/admin/objections",
                    "/admin/objections/{objectionId}"
            ).hasAuthority("ADMIN");
        }

        // 회원 + 관리자 모두 접근 가능
        private void commonEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
            auths.requestMatchers("/study/result/*")
                    .hasAnyAuthority("USER", "ADMIN");
        }



    }
