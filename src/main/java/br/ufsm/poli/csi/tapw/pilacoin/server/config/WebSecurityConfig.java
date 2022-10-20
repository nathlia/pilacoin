package br.ufsm.poli.csi.tapw.pilacoin.server.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {//extends WebSecurityConfigurerAdapter {

    //@SneakyThrows
    /*protected void configure(HttpSecurity httpSecurity) {
        httpSecurity.csrf().disable().authorizeRequests()
                .antMatchers("/websocket/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        httpSecurity.cors();

    }*/

}
