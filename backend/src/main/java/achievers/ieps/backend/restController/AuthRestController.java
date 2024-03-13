package achievers.ieps.backend.restController;

import achievers.ieps.backend.dto.UserMapper;
import achievers.ieps.backend.dto.request.LoginJwtRequestDTO;
import achievers.ieps.backend.dto.response.LoginJwtResponseDTO;
import achievers.ieps.backend.security.jwt.JwtUtils;
import achievers.ieps.backend.service.RoleService;
import achievers.ieps.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class AuthRestController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/auth/login-jwt")
    public ResponseEntity<?> loginJwtAdmin(@RequestBody LoginJwtRequestDTO loginJwtRequestDTO) {
        try {
            ResponseEntity<?> jwtToken = userService.loginJwt(loginJwtRequestDTO);
            if (jwtToken.getBody() == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(jwtToken, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/auth/token")
    public ResponseEntity<LoginJwtResponseDTO> token(@RequestBody LoginJwtRequestDTO loginJwtRequestDTO) {
        if (loginJwtRequestDTO != null) {
            return ResponseEntity.ok(new LoginJwtResponseDTO(jwtUtils.generateJwtToken(loginJwtRequestDTO.getEmail())));
        }
        return null;
    }
}

