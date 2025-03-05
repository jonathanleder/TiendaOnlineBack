package com.distribuidor.reparto.configuracion;

import com.distribuidor.reparto.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var jwtTokenOptional = getTokenFromRequest(request);

        jwtTokenOptional.ifPresent(jwtToken ->{
                if(JwtUtils.validateToken(jwtToken)){
                    var usernameOptional= JwtUtils.getUsernameFromToken(jwtToken);
                    usernameOptional.ifPresent(username -> {
                       var userDetails= userDetailsService.loadUserByUsername(username);

                       var authenticationToken= new UsernamePasswordAuthenticationToken(
                               userDetails,null,userDetails.getAuthorities()
                       );
                       authenticationToken.setDetails(
                               new WebAuthenticationDetailsSource().buildDetails(request)
                       );
                       SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    });
                }
        });

        filterChain.doFilter(request, response);
    }
    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.substring(7));
        }
        return Optional.empty();
    }
}
