package app.web.pavelk.read2.config;


import app.web.pavelk.read2.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    public static final String[] swagger_path = {"/v3/api-docs/**", "/swagger-ui/**"};
    public static final String[] open_path = {"/api/auth/**"};
    public static final String[] open_path_get = {"/api/subreddit/**", "/api/posts/**", "/api/comments/by-post/**"};

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeRequests()
                .antMatchers(open_path_get).permitAll()
                .antMatchers(open_path).permitAll()
                .antMatchers(swagger_path).permitAll()
                .anyRequest().authenticated().and()
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
