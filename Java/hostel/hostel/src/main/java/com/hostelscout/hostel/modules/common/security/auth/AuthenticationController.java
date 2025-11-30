package com.hostelscout.hostel.modules.common.security.auth;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/adminlogin")
    public Object adminLogin(@RequestBody LoginRequest request) {
        // TODO: Authenticate and return JWT token
        return authenticationService.authenticateAdmin(request);
    }
}
