package br.ufsm.poli.csi.tapw.pilacoin.server.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        System.out.println(requestTokenHeader); // = null

        String url = request.getRequestURI();
        System.out.println("Requisition Filter: " + url);


        String token = request.getHeader("Authorization");
        System.out.println("Token: " + token);

        String username = null;
        String jwtToken = null;

        // JWT Token está no form "Bearer token". Remova a palavra Bearer e
        // pegue somente o Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            System.out.println(jwtToken);
            jwtToken = requestTokenHeader.substring(7);
            System.out.println(jwtToken);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                System.out.println(jwtToken);
                System.out.println(username);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        // Tendo o token, valide o.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey("giflex")
                .parseClaimsJws(token.replace("Bearer", ""))
                .getBody();
    }

}
