package com.dance.mo.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

///every time we send  a request we want our filter to start once extend genericFilterBean, generic implements the filter interface
///we can itercept any request and extract data from it and provide data within response like adding header on response

@Component
@RequiredArgsConstructor
//// Once: we ant every request authenticated, session stateless
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
    ////when we make a call we need to pass jwt within  header called authorization
        ///we extract the header which is part of our request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        ///checking the token
        if (authHeader== null ||!authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt = authHeader.substring(7); //Bearer with space are 7
        //  extract email from token from jwtservice;
        userEmail = jwtService.extractUsername(jwt);
        //check email is found and the user is authenticated
        if (userEmail!= null && SecurityContextHolder.getContext().getAuthentication() == null){
            ///after validating user we need to check user exist within database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            //if token is valid we update security context and send request to dispatcher, by creating object username authentication password
            if (jwtService.isTokenValid(jwt, userDetails) ){
                ///object needed by security context holder to update context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(///addition details built from our request
                        new WebAuthenticationDetailsSource().buildDetails(request));
                ///update of context holder
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }
        filterChain.doFilter(request,response);
    }
}
