package achievers.ieps.backend.restcontroller;

import achievers.ieps.backend.dto.request.AdminCreateUserRequestDTO;
import achievers.ieps.backend.dto.request.AdminUpdateUserRequestDTO;
import achievers.ieps.backend.dto.request.UpdateVendorRequestDTO;
import achievers.ieps.backend.dto.response.ResponseAPI;
import achievers.ieps.backend.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private AdminService adminService;

    // Get All User
    @GetMapping(value = "/view-all")
    public ResponseAPI restGetAllUserModel(HttpServletRequest request) {

        // AUTH: TODO

        // Logic
        var response = new ResponseAPI<>();
        try {
            response.setResult(adminService.getAllUserModel());
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.name());
        } catch (NoSuchElementException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage(HttpStatus.NOT_FOUND.name());
            response.setError(e.getMessage());
        }
        return response;
    }

    // Get User By ID
    @GetMapping(value="/profile-proc-admin/{id}")
    public ResponseAPI restGetProfileById(HttpServletRequest request, @PathVariable("id") UUID id) {

        // AUTH: TODO

        // Logic
        var response = new ResponseAPI<>();
        try {
            response.setResult(adminService.getUserById(id));
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.name());
        } catch (NoSuchElementException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage(HttpStatus.NOT_FOUND.name());
            response.setError(e.getMessage());
        }
        return response;
    }

    @GetMapping(value="/profile-vendor/{id}")
    public ResponseAPI restGetVendorById(HttpServletRequest request, @PathVariable("id") UUID id) {

        // AUTH: TODO


        // Logic
        var response = new ResponseAPI<>();
        try {
            response.setResult(adminService.getVendorById(id));
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.name());
        } catch (NoSuchElementException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage(HttpStatus.NOT_FOUND.name());
            response.setError(e.getMessage());
        }
        return response;
    }

    @PostMapping(value="/add-user")
    public ResponseAPI addUser(@Valid @RequestBody AdminCreateUserRequestDTO adminCreateUserRequestDTO,
                               BindingResult bindingResult) {

        // TODO: Auth

        var response = new ResponseAPI<>();
        if (bindingResult.hasErrors()) {
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < bindingResult.getErrorCount(); i++) {
                res.append(bindingResult.getFieldErrors().get(i).getDefaultMessage());
                if (i != bindingResult.getErrorCount() - 1)
                    res.append(", ");
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.name());
            response.setError(res.toString());
            return response;
        }

        try {
            String emailUserBaru = adminCreateUserRequestDTO.getEmail();
            if (adminService.getAllUserModel().stream().anyMatch(b -> b.getEmail().equals(emailUserBaru))) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Email sudah terdaftar");
            }else{
                response.setStatus(HttpStatus.OK.value());
                response.setMessage(HttpStatus.OK.name());
                response.setResult(adminService.createUserModel(adminCreateUserRequestDTO));
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.name());
            response.setError(e.getMessage());
        }

        return response;
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseAPI deleteUser(@PathVariable(value = "id") UUID id, HttpServletRequest request) {

        //TODO: Auth

        var response = new ResponseAPI<>();
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.name());
            adminService.deleteUser(id);
            response.setResult("User has been deleted.");
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.name());
            response.setError(e.getMessage());
        }

        return response;
    }

    @PutMapping(value="/update-user-admin-proc")
    public ResponseAPI updateUserAdminProc(@Valid @RequestBody AdminUpdateUserRequestDTO userDTO,
                                     BindingResult bindingResult) {
        var response = new ResponseAPI<>();

        if (bindingResult.hasErrors()) {
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < bindingResult.getErrorCount(); i++) {
                res.append(bindingResult.getFieldErrors().get(i).getDefaultMessage());
                if (i != bindingResult.getErrorCount() - 1)
                    res.append(", ");
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.name());
            response.setError(res.toString());
            return response;
        }

        String emailUserBaru = userDTO.getEmail();
        String idUserUpdating = userDTO.getId().toString();

        if (adminService.getAllUserModel().stream()
                .anyMatch(b -> b.getEmail().equals(emailUserBaru) && !idUserUpdating.equals(b.getId()))) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Email sudah terdaftar");
            response.setResult(adminService.getUserById(userDTO.getId()));
        }else{
            try {
                response.setStatus(HttpStatus.OK.value());
                response.setMessage(HttpStatus.OK.name());
                response.setResult(adminService.updateUser(userDTO));
            } catch (NoSuchElementException e) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage(HttpStatus.NOT_FOUND.name());
                response.setError(e.getMessage());
            } catch (Exception e) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
                response.setResult(e.getMessage());
            }
        }

        return response;
    }

    @PutMapping(value="/update-user-vendor")
    public ResponseAPI updateUserVendor(@Valid @RequestBody UpdateVendorRequestDTO userDTO,
                                           BindingResult bindingResult) {
        var response = new ResponseAPI<>();

        if (bindingResult.hasErrors()) {
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < bindingResult.getErrorCount(); i++) {
                res.append(bindingResult.getFieldErrors().get(i).getDefaultMessage());
                if (i != bindingResult.getErrorCount() - 1)
                    res.append(", ");
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.name());
            response.setError(res.toString());
            return response;
        }

        String emailUserBaru = userDTO.getEmail();
        String idUserUpdating = userDTO.getId().toString();

        if (adminService.getAllUserModel().stream()
                .anyMatch(b -> b.getEmail().equals(emailUserBaru) && !idUserUpdating.equals(b.getId()))) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Email sudah terdaftar");
            response.setResult(adminService.getUserById(userDTO.getId()));
        }else{
            try {
                response.setStatus(HttpStatus.OK.value());
                response.setMessage(HttpStatus.OK.name());
                response.setResult(adminService.updateVendor(userDTO));

            } catch (NoSuchElementException e) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage(HttpStatus.NOT_FOUND.name());
                response.setError(e.getMessage());
            } catch (Exception e) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
                response.setResult(e.getMessage());
            }
        }

        return response;
    }


}
