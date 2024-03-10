package achievers.ieps.frontend.controller;

import achievers.ieps.frontend.dto.request.CreateVendorRequestDTO;
import achievers.ieps.frontend.dto.request.LoginJwtRequestDTO;
import achievers.ieps.frontend.dto.request.PasswordRequestDTO;
import achievers.ieps.frontend.dto.response.LoginJwtResponseDTO;
import achievers.ieps.frontend.dto.response.UserModelResponseDTO;
import achievers.ieps.frontend.restservice.BerkasRestService;
import achievers.ieps.frontend.restservice.UserRestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    UserRestService userRestService;

    @Autowired
    BerkasRestService berkasRestService;

    @GetMapping("/")
    public String home(){
        return "success";
    }

    @GetMapping("/register")
    public String formRegisterVendor(Model model){
        var vendorDTO = new CreateVendorRequestDTO();
        model.addAttribute("vendorDTO", vendorDTO);
        return "form-register";
    }

    @GetMapping("/validasi-vendor")
    public String validasiVendorPage(HttpServletRequest httprequest, Model model) throws IOException, InterruptedException {
        var listVendor = userRestService.getAllVendor();
        model.addAttribute("listVendor", listVendor);

        var token = httprequest.getSession().getAttribute("token").toString();
        LoginJwtResponseDTO tokenObj = new LoginJwtResponseDTO();
        tokenObj.setToken(token);
        var session = userRestService.viewProfile(tokenObj);
        model.addAttribute("user", session);
        
        return "validasi-vendor.html";
    }

    @GetMapping("/vendor/{id}")
    public String detailVendor(HttpServletRequest httprequest, @PathVariable(value = "id") UUID id, Model model) throws IOException, InterruptedException {
        var token = httprequest.getSession().getAttribute("token").toString();
        var vendor = userRestService.getProfilVendor(token, id);
        model.addAttribute("vendor", vendor);

        var listBerkas = berkasRestService.retrieveAllBerkasById(token, id.toString());
        model.addAttribute("listBerkas", listBerkas);

        LoginJwtResponseDTO tokenObj = new LoginJwtResponseDTO();
        tokenObj.setToken(token);
        var session = userRestService.viewProfile(tokenObj);
        model.addAttribute("user", session);

        return "validasi-profil-vendor.html";
    }

    @GetMapping("/vendor/{id}/validasi/{status}")
    public String updateStatusVendor(HttpServletRequest httprequest, @PathVariable(value = "id") UUID id, @PathVariable String status, Model model) throws IOException, InterruptedException {
        var token = httprequest.getSession().getAttribute("token").toString();
        var vendor = userRestService.updateProfilVendor(status, token, id);
        model.addAttribute("vendor", vendor);

        LoginJwtResponseDTO tokenObj = new LoginJwtResponseDTO();
        tokenObj.setToken(token);
        var session = userRestService.viewProfile(tokenObj);
        model.addAttribute("user", session);
        
        return "validasi-profil-vendor.html";
    }

    @GetMapping("/login-form")
    public String loginForm(Model model){
        var loginDTO = new LoginJwtRequestDTO();
        model.addAttribute("loginDTO", loginDTO);
        return "login.html";
    }

    @PostMapping("/login")
    public String loginUser(HttpServletRequest httprequest, @Valid @ModelAttribute LoginJwtRequestDTO loginDTO, BindingResult bindingResult, Model model) throws IOException, InterruptedException, JSONException {
        var token = userRestService.getToken(httprequest, loginDTO.getEmail(), loginDTO.getPassword());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (token == null){
            model.addAttribute("error", "Login gagal. Pastikan email dan kata sandi Anda benar.");
            model.addAttribute("loginDTO", new LoginJwtRequestDTO());
            return "login.html";
        }

        if (authentication != null && authentication.isAuthenticated()){
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.contains(new SimpleGrantedAuthority("VENDOR_NOT_VALID"))){
                return "redirect:/profile/view";
            } else if (authorities.contains(new SimpleGrantedAuthority("VENDOR"))) {
                return "redirect:/profile/view";
            } else if (authorities.contains(new SimpleGrantedAuthority("PROCSTAFF"))) {
                return "redirect:/profile/view";
            } else if (authorities.contains(new SimpleGrantedAuthority("PROCMANAGER"))) {
                return "redirect:/profile/view";
            } else if (authorities.contains(new SimpleGrantedAuthority("ADMIN"))){
                return "redirect:/admin/view-user";
            }
        }
        return "test.html";
    }

    @GetMapping("/test")
    public String test(HttpServletRequest httprequest, Model model){
        var token = httprequest.getSession().getAttribute("token").toString();
        System.out.println("Ini dari test.html");
        System.out.println(token);
        return "test.html";
    }

    @PostMapping("/register")
    public String registrasiVendor(HttpServletRequest httprequest,
                                   @Valid @ModelAttribute CreateVendorRequestDTO vendorDTO,
                                   BindingResult bindingResult,
                                   Model model) throws IOException, InterruptedException, JSONException {
        if (bindingResult.hasErrors()){
            List<String> errorMessages = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            model.addAttribute("error", errorMessages);
            model.addAttribute("vendorDTO", new CreateVendorRequestDTO());
            return "form-register.html";
        }
        var output = userRestService.registerVendor(vendorDTO);
        if (output.isEmpty()) {
            model.addAttribute("error", "Maaf, pengguna dengan email ini sudah tercatat dalam sistem");
            model.addAttribute("vendorDTO", new CreateVendorRequestDTO());
            return "form-register.html";
        } else {
            userRestService.getToken(httprequest, vendorDTO.getEmail(), vendorDTO.getPassword());
            return "success";
        }
    }

    @GetMapping("/profile/view")
    public String viewProfile(HttpServletRequest httprequest, Model model) throws IOException, InterruptedException, JSONException{
        var token = httprequest.getSession().getAttribute("token").toString();
        LoginJwtResponseDTO tokenObj = new LoginJwtResponseDTO();
        tokenObj.setToken(token);
        var session = userRestService.viewProfile(tokenObj);
        if (session.getRole().equals("Vendor")) {
            model.addAttribute("vendor", session);
            model.addAttribute("passwordDTO", new PasswordRequestDTO());
            return "view-profil-vendor.html";
        } else {
            model.addAttribute("user", session);
            model.addAttribute("passwordDTO", new PasswordRequestDTO());
            return "view-profil-user.html";
        }
    }

    @GetMapping("/profile/edit")
    public String editProfile(HttpServletRequest httprequest, Model model) throws IOException, InterruptedException, JSONException{
        var token = httprequest.getSession().getAttribute("token").toString();
        LoginJwtResponseDTO tokenObj = new LoginJwtResponseDTO();
        tokenObj.setToken(token);
        var session = userRestService.viewProfile(tokenObj);
        if (session.getRole().equals("Vendor")) {
            model.addAttribute("vendor", session);
            return "update-profil-vendor.html";
        } else {
            model.addAttribute("user", session);
            return "update-profil-user.html";
        }
    }

    @PostMapping("/profile/edit")
    public String saveEditProfile(HttpServletRequest httprequest, @Valid @ModelAttribute UserModelResponseDTO userModelResponseDTO, BindingResult bindingResult, Model model) throws IOException, InterruptedException, JSONException {
        var token = httprequest.getSession().getAttribute("token").toString();
        LoginJwtResponseDTO tokenObj = new LoginJwtResponseDTO();
        tokenObj.setToken(token);
        var user = userRestService.viewProfile(tokenObj);
        userModelResponseDTO.setEmailToken(user.getEmail());

        if (bindingResult.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            model.addAttribute("error", errorMessages);
            if (user.getRole().equals("Vendor")) {
                model.addAttribute("vendor", user);
                return "update-profil-vendor.html";
            } else {
                model.addAttribute("user", user);
                return "update-profil-user.html";
            }
        } else {
            UserModelResponseDTO dto = userRestService.updateProfile(httprequest, userModelResponseDTO, token);
            if (dto != null) {
                model.addAttribute("success", "Data perubahan berhasil disimpan");
                if (dto.getRole().equals("Vendor")) {
                    model.addAttribute("vendor", dto);
                    model.addAttribute("passwordDTO", new PasswordRequestDTO());
                    return "view-profil-vendor.html";
                } else {
                    model.addAttribute("user", dto);
                    model.addAttribute("passwordDTO", new PasswordRequestDTO());
                    return "view-profil-user.html";
                }
            } else {
                // Handle update failure
                model.addAttribute("error", "Email digunakan oleh user lain");
                if (user.getRole().equals("Vendor")) {
                    model.addAttribute("vendor", user);
                    return "update-profil-vendor.html";
                } else {
                    model.addAttribute("user", user);
                    return "update-profil-user.html";
                }
            }
        }
    }

    @PostMapping("/profile/edit/password")
    public String saveEditProfilePassword(HttpServletRequest httprequest, @Valid @ModelAttribute PasswordRequestDTO passwordDTO, BindingResult bindingResult, Model model) throws IOException, InterruptedException, JSONException{
        var token = httprequest.getSession().getAttribute("token").toString();
        LoginJwtResponseDTO tokenObj = new LoginJwtResponseDTO();
        tokenObj.setToken(token);
        var user = userRestService.viewProfile(tokenObj);
        if (bindingResult.hasErrors()){
            List<String> errorMessages = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            model.addAttribute("error", errorMessages);
            if (user.getRole().equals("Vendor")) {
                model.addAttribute("vendor", user);
                model.addAttribute("passwordDTO", new PasswordRequestDTO());
                return "view-profil-vendor.html";
            } else {
                model.addAttribute("user", user);
                model.addAttribute("passwordDTO", new PasswordRequestDTO());
                return "view-profil-user.html";
            }
        } else {
            UserModelResponseDTO dto = userRestService.updateProfilePassword(passwordDTO, token);
            if (dto == null){
                model.addAttribute("error", "Mohon ulangi, Password tidak sesuai dengan yang dikonfirmasi atau Password Baru sama dengan Password lama");
                if (user.getRole().equals("Vendor")) {
                    model.addAttribute("vendor", user);
                    model.addAttribute("passwordDTO", new PasswordRequestDTO());
                    return "view-profil-vendor.html";
                } else {
                    model.addAttribute("user", user);
                    model.addAttribute("passwordDTO", new PasswordRequestDTO());
                    return "view-profil-user.html";
                }
            }
            model.addAttribute("success", "Password berhasil diubah");
            if (dto.getRole().equals("Vendor")){
                model.addAttribute("vendor", dto);
                model.addAttribute("passwordDTO", new PasswordRequestDTO());
                return "view-profil-vendor.html";
            } else {
                model.addAttribute("user", dto);
                model.addAttribute("passwordDTO", new PasswordRequestDTO());
                return "view-profil-user.html";
            }
        }
    }
}
