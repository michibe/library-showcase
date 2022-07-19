package org.michibe.libraryshowcase.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.michibe.libraryshowcase.api.model.CreateUserReqPayload;
import org.michibe.libraryshowcase.api.model.GetAllUsersResPayload;
import org.michibe.libraryshowcase.api.model.GetUserResPayload;
import org.michibe.libraryshowcase.api.model.UpdateUserReqPayload;
import org.michibe.libraryshowcase.modules.user.UserAlreadyExistException;
import org.michibe.libraryshowcase.modules.user.UserNotExistException;
import org.michibe.libraryshowcase.modules.user.UserService;
import org.michibe.libraryshowcase.modules.user.model.UserId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@SecurityRequirement(name = "JWT")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(Paths.CREATE_USER)
    public ResponseEntity<GetUserResPayload> createUser(
            @Valid @RequestBody CreateUserReqPayload reqPayload
    ) {
        try {
            final var createdUser = userService.createUser(reqPayload);
            return ResponseEntity.ok(GetUserResPayload.of(createdUser));
        }catch (UserAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

    }

    @GetMapping(Paths.GET_ALL_USERS)
    public ResponseEntity<GetAllUsersResPayload> getAllUsers() {
        final var allUsers = userService.getAllUsers();
        return ResponseEntity.ok(GetAllUsersResPayload.of(allUsers));
    }

    @GetMapping(Paths.GET_USER)
    public ResponseEntity<GetUserResPayload> getUser(
            @PathVariable("userId") UserId userId
    ) {
        final var user = userService.getUser(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(GetUserResPayload.of(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(Paths.UPDATE_USER)
    public ResponseEntity<GetUserResPayload> updateUser(
            @PathVariable("userId") UserId userId,
            @Valid @RequestBody UpdateUserReqPayload reqPayload
    ) {
        try {
            final var updatedUser = userService.updateUser(userId, reqPayload);
            return ResponseEntity.ok(GetUserResPayload.of(updatedUser));
        } catch (UserNotExistException userNotExistException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(Paths.DELETE_USER)
    public ResponseEntity deleteUser(
            @PathVariable("userId") UserId userId
    ) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (UserNotExistException userNotExistException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}