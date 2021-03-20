package es.udc.fic.ginecologia;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import es.udc.fic.ginecologia.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
		
		http.addFilterBefore(filter,CsrfFilter.class)
			.authorizeRequests()
	        .antMatchers("/user/register").hasRole("ADMIN")
	        .antMatchers("/user/register-error").hasRole("ADMIN")
	        .antMatchers("/user/user-list").hasRole("ADMIN")
	        .antMatchers("/user/update/{id}").hasRole("ADMIN")
	        .antMatchers("/user/change-password/{id}").hasRole("ADMIN")
	        .antMatchers("/user/search").hasRole("ADMIN")
	        .antMatchers("/user/change-enabling-state/{id}").hasRole("ADMIN")
	        .antMatchers("/user/change-schedule/{id}").hasRole("ADMIN")
	        .antMatchers("/speciality/speciality-list").hasRole("ADMIN")
	        .antMatchers("/speciality/speciality-list-error").hasRole("ADMIN")
	        .antMatchers("/speciality/add-speciality").hasRole("ADMIN")
	        .antMatchers("/speciality/search-specialities").hasRole("ADMIN")
	        .antMatchers("/speciality/change-speciality-state/{id}").hasRole("ADMIN")
	        .antMatchers("/speciality/update/{id}").hasRole("ADMIN")
	        .antMatchers("/speciality/change-specialities/{id}").hasRole("ADMIN")
	        .antMatchers("/medicine/medicine-list").hasRole("ADMIN")
	        .antMatchers("/medicine/medicine-list-error").hasRole("ADMIN")
	        .antMatchers("/medicine/add-medicine").hasRole("ADMIN")
	        .antMatchers("/medicine/search-medicines").hasRole("ADMIN")
	        .antMatchers("/medicine/change-medicine-state/{id}").hasRole("ADMIN")
	        .antMatchers("/medicine/update/{id}").hasRole("ADMIN")
	        .antMatchers("/diagnostic-test/diagnostic-test-list").hasRole("ADMIN")
	        .antMatchers("/diagnostic-test/diagnostic-test-list-error").hasRole("ADMIN")
	        .antMatchers("/diagnostic-test/add-diagnostic-test").hasRole("ADMIN")
	        .antMatchers("/diagnostic-test/update/{id}").hasRole("ADMIN")
	        .antMatchers("/diagnostic-test/change-diagnostic-test-state/{id}").hasRole("ADMIN")
	        .antMatchers("/diagnostic-test/search-diagnostic-tests").hasRole("ADMIN")
	        .antMatchers("/contraceptive/contraceptive-list").hasRole("ADMIN")
	        .antMatchers("/contraceptive/contraceptive-list-error").hasRole("ADMIN")
	        .antMatchers("/contraceptive/add-contraceptive").hasRole("ADMIN")
	        .antMatchers("/contraceptive/update/{id}").hasRole("ADMIN")
	        .antMatchers("/contraceptive/change-contraceptive-state/{id}").hasRole("ADMIN")
	        .antMatchers("/contraceptive/search-contraceptives").hasRole("ADMIN")
	        .anyRequest().authenticated()
	        .and()
		        .formLogin()
	            .loginPage("/login")
	            .failureUrl("/login-error")
	            .permitAll()
	        .and()
	        .logout().logoutSuccessUrl("/").permitAll();
		
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web
	            .ignoring()
	            .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/assets/**", "/img/**", "/icon/**", "/templates/**");
	}
}
