package achievers.ieps.frontend.controller;

import achievers.ieps.frontend.dto.request.AdminCreateUserRequestDTO;
import achievers.ieps.frontend.dto.request.AdminUpdateUserRequestDTO;
import achievers.ieps.frontend.dto.request.UserModelRequestDTO;
import achievers.ieps.frontend.dto.request.VendorRequestDTO;
import achievers.ieps.frontend.dto.response.ResponseAPI;
import achievers.ieps.frontend.dto.response.admin.AdminReadUserResponseDTO;
import achievers.ieps.frontend.dto.response.admin.AdminReadUserUpdateResponseDTO;
import achievers.ieps.frontend.dto.response.admin.VendorReadUserResponseDTO;
import achievers.ieps.frontend.restservice.UserRestService;
import achievers.ieps.frontend.setting.Setting;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class AdminController {
    @Autowired
    UserRestService userRestService;

    @Autowired
    Setting setting;


    // View All User
    @GetMapping("admin/view-user")
    public String viewAllUser(Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
//        String getAllUserModelApiUrl = "http://localhost:8080/api/admin/view-all";
        String getAllUserModelApiUrl = "https://achievers-backend.up.railway.app/admin/api" + "/view-all";

        // Make HTTP request to get all user
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var token = request.getSession().getAttribute("token").toString();
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<ResponseAPI<List<UserModelRequestDTO>>> listUserModelResponse = restTemplate.exchange(
                getAllUserModelApiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseAPI<List<UserModelRequestDTO>>>() {
                });

        // Check if the response is successful and contains users
        if (listUserModelResponse.getBody() != null && listUserModelResponse.getBody().getStatus() == 200) {
            model.addAttribute("users", listUserModelResponse.getBody().getResult());
        } else {
            // Handle jika request gagal, atau response tidak berisi Users
            redirectAttributes.addFlashAttribute("error", "No user available, try again later");
            return ""; //TODO
        }

        return "admin/kelola-data-pengguna";
    }

    // View User By Id
    @GetMapping("admin/view-user-proc-admin/{id}")
    public String viewUserProcAdmin(@PathVariable("id") UUID id, Model model, RedirectAttributes redirectAttributes) {

//        String getUserModelApiUrl = "http://localhost:8080/api/admin/profile-proc-admin/" + id;
        String getUserModelApiUrl = "https://achievers-backend.up.railway.app/admin/api" + "/profile-proc-admin/" + id;

        System.out.println("MASUK GETMAPPING");

        RestTemplate restTemplate = new RestTemplate();

        // Mendapat informasi detail profil user
        ResponseEntity<ResponseAPI<UserModelRequestDTO>> userModelResponse = restTemplate.exchange(
                getUserModelApiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseAPI<UserModelRequestDTO>>() {
                });

        System.out.println(userModelResponse.getBody().getResult().getNomorTelefon());
        if (userModelResponse.getBody() != null && userModelResponse.getBody().getStatus() == 200) {
            UserModelRequestDTO user = userModelResponse.getBody().getResult();

            if(user.getRole().equals("Procurement Staff") || user.getRole().equals("Procurement Manager")){
                model.addAttribute("user", userModelResponse.getBody().getResult());
                return "admin/view-procurement";
            } else if (user.getRole().equals("Admin")) {
                model.addAttribute("user", userModelResponse.getBody().getResult());
                return "admin/view-admin";
            }

        } else {
            // Handle jika request gagal
            redirectAttributes.addFlashAttribute("error", "No user available, try again later");
            return ""; //TODO
        }
        return"";
    }

    @GetMapping("admin/view-user-vendor/{id}")
    public String viewUserVendor(@PathVariable("id") UUID id, Model model, RedirectAttributes redirectAttributes) {
//        String getVendorApiUrl = "http://localhost:8080/api/admin/profile-vendor/" + id;
        String getVendorApiUrl = "https://achievers-backend.up.railway.app/admin/api" + "/profile-vendor/" + id;

        RestTemplate restTemplate = new RestTemplate();

        // Mendapat informasi detail profil user
        ResponseEntity<ResponseAPI<VendorRequestDTO>> vendorResponse = restTemplate.exchange(
                getVendorApiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseAPI<VendorRequestDTO>>() {
                });
        if (vendorResponse.getBody() != null && vendorResponse.getBody().getStatus() == 200) {
            System.out.println("BERHASIL");

            model.addAttribute("user", vendorResponse.getBody().getResult());
            return "admin/view-vendor";

        } else {
            System.out.println("GAGAL");
            // Handle jika request gagal
            redirectAttributes.addFlashAttribute("error", "No user available, try again later");
            return ""; //TODO
        }

    }

    // Tambah Pengguna
    @GetMapping("admin/add-user")
    public String addUserPage(Model model, RedirectAttributes redirectAttributes) {
        AdminCreateUserRequestDTO newUserRequest = new AdminCreateUserRequestDTO();
        model.addAttribute("userDTO",newUserRequest);
        return "admin/add-user";
    }

    @PostMapping("admin/add-user")
    public String addUser(@ModelAttribute AdminCreateUserRequestDTO userDTO,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes,
                          Model model) throws IOException{
//        String postUserUrl = "http://localhost:8080/api/admin/add-user";
        String postUserUrl = "https://achievers-backend.up.railway.app/admin/api" + "/add-user";

        try{
            // Create a HttpHeaders object to set the content type
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            var token = request.getSession().getAttribute("token").toString();
            headers.set("Authorization", "Bearer " + token);


            HttpEntity<AdminCreateUserRequestDTO> requestEntity = new HttpEntity<>(userDTO, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<ResponseAPI<AdminReadUserResponseDTO>> response = restTemplate.exchange(
                    postUserUrl,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    });

            if (response.getBody() != null && response.getBody().getStatus().equals(200)) {
                redirectAttributes.addFlashAttribute("success", "New user added successfully");
                return "redirect:/admin/view-user?success=User baru berhasil tersimpan";
            } else if (response.getBody().getMessage().equals("Email sudah terdaftar")) {
                return "redirect:add-user?emailExist=true";
            }
            else {
                model.addAttribute("error", response.getBody().getError());
                model.addAttribute("userDTO", userDTO);
                System.out.println(response.getBody().getMessage());
                return ""; //TODO
            }

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("userDTO", userDTO);
            return ""; //TODO
        }

    }

    @GetMapping("/admin/{id}/delete-user")
    public String deleteUser(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        // TODO: Auth

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            headers.setContentType(MediaType.APPLICATION_JSON);
            RestTemplate restTemplate = new RestTemplate();

//            String deleteUserUrl = "http://localhost:8080/api/admin/delete-user/" + id;
            String deleteUserUrl = "https://achievers-backend.up.railway.app/admin/api" + "/delete-user/" + id;

            // Menghapus User berdasarkan ID
            ResponseEntity<ResponseAPI<String>> response = restTemplate.exchange(
                    deleteUserUrl,
                    HttpMethod.DELETE,
                    entity,
                    new ParameterizedTypeReference<>() {
                    });

            if (response.getBody() != null && response.getBody().getStatus().equals(200)) {
                redirectAttributes.addFlashAttribute("success", "User deleted successfully");
                return "redirect:/admin/view-user?success=User berhasil dihapus";
            } else {
                redirectAttributes.addFlashAttribute("error", response.getBody().getError());
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:view-user";
    }

    // Update User proc - admin
    @GetMapping("/admin/{id}/update-user-proc-admin")
    public String updateUserPage(@PathVariable("id") UUID id, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

//        String getUserApiUrl = "http://localhost:8080/api/admin/profile-proc-admin/" + id;
        String getUserApiUrl = "https://achievers-backend.up.railway.app/admin/api" + "/profile-proc-admin/" + id;

        // Mendapatkan informasi user untuk ditampilkan di form
        ResponseEntity<ResponseAPI<AdminUpdateUserRequestDTO>> userResponse = restTemplate.exchange(
                getUserApiUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                });

        if (userResponse.getBody() != null && userResponse.getBody().getStatus().equals(200)) {

            model.addAttribute("user", userResponse.getBody().getResult());
            AdminUpdateUserRequestDTO newUserRequest = new AdminUpdateUserRequestDTO();
            model.addAttribute("userDTO",newUserRequest);
        } else {
            System.out.println("error");
            model.addAttribute("error", "User not found, try again later");
        }
        return "admin/update-user-proc-admin";
    }

    @PostMapping("/admin/update-user-proc-admin")
    public String updateUser(
            @ModelAttribute AdminUpdateUserRequestDTO userDTO,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request
    ) throws IOException {

        // TODO: AUTH

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AdminUpdateUserRequestDTO> requestEntity = new HttpEntity<>(userDTO, headers);
        RestTemplate restTemplate = new RestTemplate();

        var token = request.getSession().getAttribute("token").toString();
        headers.set("Authorization", "Bearer " + token);


//        String updateUserUrl = "http://localhost:8080/api/admin/update-user-admin-proc";
        String updateUserUrl = "https://achievers-backend.up.railway.app/admin/api" + "/update-user-admin-proc";


        ResponseEntity<ResponseAPI<AdminReadUserUpdateResponseDTO>> response = restTemplate.exchange(
                updateUserUrl,
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        if (response.getBody() != null && response.getBody().getStatus().equals(200)) {
            redirectAttributes.addFlashAttribute("success", "User updated successfully");
            return  "redirect:/admin/view-user?success=User berhasil diperbarui";
        } else if (response.getBody().getMessage().equals("Email sudah terdaftar")){
            redirectAttributes.addFlashAttribute("error", response.getBody().getError());
            return "redirect:/admin/" + response.getBody().getResult().getId() + "/update-user-proc-admin?emailExist=true";
        } else{
            redirectAttributes.addFlashAttribute("error", response.getBody().getError());
            return "";
        }


    }

    // Update User vendor
    @GetMapping("/admin/{id}/update-user-vendor")
    public String updateVendorPage(@PathVariable("id") UUID id, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

//        String getUserApiUrl = "http://localhost:8080/api/admin/profile-vendor/" + id;
        String getUserApiUrl = "https://achievers-backend.up.railway.app/admin/api" + "/profile-vendor/" + id;

        // Mendapatkan informasi user untuk ditampilkan di form
        ResponseEntity<ResponseAPI<VendorRequestDTO>> userResponse = restTemplate.exchange(
                getUserApiUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                });

        if (userResponse.getBody() != null && userResponse.getBody().getStatus().equals(200)) {
            System.out.println(userResponse.getBody().getResult().getStatus());

            model.addAttribute("user", userResponse.getBody().getResult());
            VendorRequestDTO newUserRequest = new VendorRequestDTO();
            model.addAttribute("userDTO",newUserRequest);
        } else {
            System.out.println("error");
            model.addAttribute("error", "User not found, try again later");
        }
        return "admin/update-user-vendor";
    }

    @PostMapping("/admin/update-user-vendor")
    public String updateVendor(
            @ModelAttribute VendorRequestDTO userDTO,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request
    ) throws IOException {

        // TODO: AUTH

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<VendorRequestDTO> requestEntity = new HttpEntity<>(userDTO, headers);
        RestTemplate restTemplate = new RestTemplate();

        var token = request.getSession().getAttribute("token").toString();
        headers.set("Authorization", "Bearer " + token);

//        String updateUserUrl = "http://localhost:8080/api/admin/update-user-vendor";
        String updateUserUrl = "https://achievers-backend.up.railway.app/admin/api" + "/update-user-vendor";

        System.out.println("MASUK UPDATE POST");
        ResponseEntity<ResponseAPI<VendorReadUserResponseDTO>> response = restTemplate.exchange(
                updateUserUrl,
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        if (response.getBody() != null && response.getBody().getStatus().equals(200)) {
            redirectAttributes.addFlashAttribute("success", "User updated successfully");
            return  "redirect:/admin/view-user?success=User berhasil diperbarui";
        } else if (response.getBody().getMessage().equals("Email sudah terdaftar")){
            redirectAttributes.addFlashAttribute("error", response.getBody().getError());
            return "redirect:/admin/" + response.getBody().getResult().getId() + "/update-user-vendor?emailExist=true";
        } else{
            redirectAttributes.addFlashAttribute("error", response.getBody().getError());
            return "";
        }


    }


}
