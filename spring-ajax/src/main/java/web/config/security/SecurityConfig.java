package web.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import web.filter.GoogleOauthRedirectFilter;

import javax.servlet.Filter;


@Configuration
@ComponentScan("web")
@EnableOAuth2Client
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2RestTemplate oAuth2RestTemplate;
    private final CustomUserInfoTokenService tokenService;
    private final UserAuthenticationSuccessHandler authenticationSuccessHandler;
    private final GoogleOauthRedirectFilter googleOauthRedirectFilter;

    @Autowired
    public SecurityConfig( OAuth2RestTemplate auth2RestTemplate, CustomUserInfoTokenService tokenService, UserAuthenticationSuccessHandler authenticationSuccessHandler, GoogleOauthRedirectFilter googleOauthRedirectFilter) {

        this.oAuth2RestTemplate = auth2RestTemplate;
        this.tokenService = tokenService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.googleOauthRedirectFilter = googleOauthRedirectFilter;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("api/admin/*").hasAuthority("ADMIN")
                .antMatchers("api/user/*").hasAuthority("USER")
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_process")
                .failureUrl("/login?error")
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .permitAll()
                .successHandler(authenticationSuccessHandler);

        http.logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true);

        http.addFilterBefore(googleOauthFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    private Filter googleOauthFilter() {
        OAuth2ClientAuthenticationProcessingFilter googleFilter =
                new OAuth2ClientAuthenticationProcessingFilter("/user");
        googleFilter.setRestTemplate(oAuth2RestTemplate);
        googleFilter.setTokenServices(tokenService);
        return googleFilter;
    }

    @Bean
    public FilterRegistrationBean<GoogleOauthRedirectFilter> someFilterRegistration() {
        FilterRegistrationBean<GoogleOauthRedirectFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(googleOauthRedirectFilter);
        registration.addUrlPatterns("/");
        return registration;
    }

}
