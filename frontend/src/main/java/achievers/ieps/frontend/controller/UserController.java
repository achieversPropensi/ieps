package achievers.ieps.frontend.controller;

import achievers.ieps.frontend.dto.request.CreateVendorRequestDTO;
import achievers.ieps.frontend.dto.request.LoginJwtRequestDTO;
import achievers.ieps.frontend.dto.response.VendorResponseDTO;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {
    @Autowired
    UserRestService userRestService;

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
    public String validasiVendorPage(Model model) throws IOException, InterruptedException {
        var listVendor = userRestService.getAllVendor();
        model.addAttribute("listVendor", listVendor);
        return "validasi-vendor.html";
    }

    @GetMapping("/vendor/{id}")
    public String detailVendor(HttpServletRequest httprequest, @PathVariable(value = "id") UUID id, Model model) throws IOException, InterruptedException {
        var token = httprequest.getSession().getAttribute("token").toString();
        var vendor = userRestService.getProfilVendor(token, id);
        model.addAttribute("vendor", vendor);
        return "profil-vendor.html";
    }

    @GetMapping("/vendor/{id}/validasi/{status}")
    public String updateStatusVendor(HttpServletRequest httprequest, @PathVariable(value = "id") UUID id, @PathVariable String status, Model model) throws IOException, InterruptedException {
        var token = httprequest.getSession().getAttribute("token").toString();
        var vendor = userRestService.updateProfilVendor(status, token, id);
        model.addAttribute("vendor", vendor);
        return "profil-vendor.html";
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
                //TODO connect to page accordingly
            } else if (authorities.contains(new SimpleGrantedAuthority("VENDOR"))) {
                //TODO connect to page accordingly
            } else if (authorities.contains(new SimpleGrantedAuthority("PROCSTAFF"))) {
                //TODO connect to page accordingly
            } else if (authorities.contains(new SimpleGrantedAuthority("PROCMANAGER"))) {
                //TODO connect to page accordingly
            } else if (authorities.contains(new SimpleGrantedAuthority("ADMIN"))){
                //TODO connect to page accordingly
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
}
