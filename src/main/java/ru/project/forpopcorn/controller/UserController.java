package ru.project.forpopcorn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.project.forpopcorn.dto.UserDTO;
import ru.project.forpopcorn.mapper.UserMapper;
import ru.project.forpopcorn.service.UserService;
import ru.project.forpopcorn.validations.ResponseErrorValidation;

import javax.validation.Valid;
import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper, ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal){
        UserDTO userDTO = userMapper.convertToUserDTO(userService.getCurrentUser(principal));
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id){
        UserDTO userDTO = userMapper.convertToUserDTO(userService.getUserById(id));
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        UserDTO userDTO1 = userMapper.convertToUserDTO(userService.updateUser(userDTO, principal));
        return new ResponseEntity<>(userDTO1, HttpStatus.OK);
    }
}
