package org.michibe.libraryshowcase.api;

import org.michibe.libraryshowcase.api.model.CreateAccessTokenReqPayload;
import org.michibe.libraryshowcase.api.model.CreateAccessTokenResPayload;
import org.michibe.libraryshowcase.configuration.security.JwtUtil;
import org.michibe.libraryshowcase.modules.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
record TokenRestController(
        UserService userService,
        AuthenticationManager authenticationManager,
        JwtUtil jwtUtil
) {

    @PostMapping(Paths.CREATE_ACCESS_TOKEN)
    public ResponseEntity<CreateAccessTokenResPayload> createAccessToken(
            @Valid @RequestBody CreateAccessTokenReqPayload reqPayload
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(reqPayload.email(), reqPayload.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);

        return ResponseEntity.ok(new CreateAccessTokenResPayload(jwt));
    }

}