package achievers.ieps.frontend.controller;

import achievers.ieps.frontend.dto.request.*;
import achievers.ieps.frontend.dto.response.BerkasInfoResponseDTO;
import achievers.ieps.frontend.dto.response.KonfigurasiBerkasResponseDTO;
import achievers.ieps.frontend.dto.response.LoginJwtResponseDTO;
import achievers.ieps.frontend.dto.response.LoginJwtResponseDTO;
import achievers.ieps.frontend.restservice.BerkasRestService;
import achievers.ieps.frontend.restservice.UserRestService;
import achievers.ieps.frontend.restservice.VendorRestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BerkasController {
    @Autowired
    BerkasRestService berkasRestService;
    @Autowired
    VendorRestService vendorRestService;
    @Autowired
    UserRestService userRestService;

    @GetMapping("/vendor-assessment")
    public String vendorAssessment(HttpServletRequest request, Model model) throws IOException, InterruptedException {
        var token = request.getSession().getAttribute("token").toString();


        var check = vendorRestService.checkInfo(token);
        model.addAttribute("vendorInfo", check);

        var user = userRestService.viewProfile(new LoginJwtResponseDTO(token));
        model.addAttribute("vendor", user);

        if (!check.isHasSubmitted()) {
            return "redirect:/vendor-assessment/form";
        }

        var listBerkas = berkasRestService.retrieveAllBerkas(token);
        model.addAttribute("listBerkas", listBerkas);

        return "view-va.html";
    }

    @GetMapping("/vendor-assessment/form")
    public String vaForm(HttpServletRequest request, Model model) throws IOException, InterruptedException {
        var token = request.getSession().getAttribute("token").toString();
        var check = vendorRestService.checkInfo(token);
        model.addAttribute("vendorInfo", check);

        if (check.isHasSubmitted()) {
            return "redirect:/vendor-assessment";
        }

        var user = userRestService.viewProfile(new LoginJwtResponseDTO(token));
        model.addAttribute("vendor", user);

        var listKonfigurasi = berkasRestService.retrieveKonfigurasi(token);
        model.addAttribute("listKonfigurasi", listKonfigurasi);

        UploadBerkasFormDTO berkasFormDTO = new UploadBerkasFormDTO(); // Replace with your actual BerkasFormDTO instantiation
        List<UploadBerkasDTO> listBerkas = new ArrayList<>();

        for (KonfigurasiBerkasResponseDTO konfigurasi : listKonfigurasi) {
            UploadBerkasDTO berkas = new UploadBerkasDTO();
            berkas.setNama(konfigurasi.getNamaBerkas());
            berkas.setDeskripsi(konfigurasi.getDeskripsi());
            listBerkas.add(berkas);
        }

        berkasFormDTO.setListBerkas(listBerkas);
        model.addAttribute("berkasFormDTO", berkasFormDTO);

        return "form-va.html";
    }

    @PostMapping("/vendor-assessment/upload")
    public String vaSubmit(HttpServletRequest request,
                           @Valid @ModelAttribute UploadBerkasFormDTO uploadBerkasFormDTO,
                           BindingResult bindingResult, Model model)
            throws IOException, InterruptedException, JSONException {

        var token = request.getSession().getAttribute("token").toString();
        List<String> errorMessages = new ArrayList<>();

        var check = vendorRestService.checkInfo(token);
        model.addAttribute("vendorInfo", check);

        var user = userRestService.viewProfile(new LoginJwtResponseDTO(token));
        model.addAttribute("vendor", user);

        if (bindingResult.hasErrors()){
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            model.addAttribute("error", errorMessages);
            System.out.println("Masuk BINDING RESULTS ERROR ya...");
            var listKonfigurasi = berkasRestService.retrieveKonfigurasi(token);
            model.addAttribute("listKonfigurasi", listKonfigurasi);

            UploadBerkasFormDTO berkasFormDTO = new UploadBerkasFormDTO(); // Replace with your actual BerkasFormDTO instantiation
            List<UploadBerkasDTO> listBerkas = new ArrayList<>();

            for (KonfigurasiBerkasResponseDTO konfigurasi : listKonfigurasi) {
                UploadBerkasDTO berkas = new UploadBerkasDTO();
                berkas.setNama(konfigurasi.getNamaBerkas());
                berkas.setDeskripsi(konfigurasi.getDeskripsi());
                listBerkas.add(berkas);
            }

            berkasFormDTO.setListBerkas(listBerkas);
            model.addAttribute("berkasFormDTO", berkasFormDTO);

            return "form-va.html";
        }


        for (UploadBerkasDTO berkasDTO: uploadBerkasFormDTO.getListBerkas()) {
            var createBerkasDTO = new CreateBerkasRequestDTO();
            createBerkasDTO.setNama(berkasDTO.getNama());
            createBerkasDTO.setDeskripsi(berkasDTO.getDeskripsi());
            createBerkasDTO.setJudul(berkasDTO.getFile().getOriginalFilename());
            createBerkasDTO.setType(berkasDTO.getFile().getContentType());
            createBerkasDTO.setData(berkasDTO.getFile().getBytes());
            createBerkasDTO.setSize(berkasDTO.getFile().getSize());
            createBerkasDTO.setToken(token);

            var output = berkasRestService.uploadBerkas(token, createBerkasDTO);

            if (output.containsKey("error")) {
                errorMessages.add(output.get("error"));
                model.addAttribute("error", errorMessages);

                var listKonfigurasi = berkasRestService.retrieveKonfigurasi(token);
                model.addAttribute("listKonfigurasi", listKonfigurasi);

                UploadBerkasFormDTO berkasFormDTO = new UploadBerkasFormDTO(); // Replace with your actual BerkasFormDTO instantiation
                List<UploadBerkasDTO> listBerkas = new ArrayList<>();

                for (KonfigurasiBerkasResponseDTO konfigurasi : listKonfigurasi) {
                    UploadBerkasDTO berkas = new UploadBerkasDTO();
                    berkas.setNama(konfigurasi.getNamaBerkas());
                    berkas.setDeskripsi(konfigurasi.getDeskripsi());
                    berkas.setDeskripsi(konfigurasi.getDeskripsi());
                    listBerkas.add(berkas);
                }

                berkasFormDTO.setListBerkas(listBerkas);
                model.addAttribute("berkasFormDTO", berkasFormDTO);

                return "form-va.html";
            }
        }
        var submissionChange = vendorRestService.toggleSubmission(token);
        return "redirect:/vendor-assessment";
    }

    @GetMapping("/vendor-assessment/edit")
    public String vaEditForm(HttpServletRequest request, Model model) throws IOException, InterruptedException {
        var token = request.getSession().getAttribute("token").toString();
        var check = vendorRestService.checkInfo(token);
        model.addAttribute("vendorInfo", check);

        if (!check.isHasSubmitted()) {
            return "redirect:/vendor-assessment/form";
        }

        if (!check.getStatus().equals("Gagal Terverifikasi")) {
            return "redirect:/vendor-assessment";
        }

        var user = userRestService.viewProfile(new LoginJwtResponseDTO(token));
        model.addAttribute("vendor", user);

        var listBerkasExisting = berkasRestService.retrieveAllBerkas(token);
        model.addAttribute("listBerkasExisting", listBerkasExisting);

        UpdateBerkasFormDTO berkasFormDTO = new UpdateBerkasFormDTO();
        List<UpdateBerkasDTO> listBerkasInDTO = new ArrayList<>();

        for (BerkasInfoResponseDTO berkasInfo : listBerkasExisting) {
            UpdateBerkasDTO berkas = new UpdateBerkasDTO();
            berkas.setId(berkasInfo.getId());
            berkas.setNama(berkasInfo.getNama());
            berkas.setDeskripsi(berkasInfo.getDeskripsi());
            listBerkasInDTO.add(berkas);
        }

        berkasFormDTO.setListBerkas(listBerkasInDTO);
        model.addAttribute("berkasFormDTO", berkasFormDTO);

        return "form-va-edit.html";

    }

    @PostMapping("/vendor-assessment/update")
    public String vaEditSubmit(HttpServletRequest request,
                           @Valid @ModelAttribute UpdateBerkasFormDTO updateBerkasFormDTO,
                           BindingResult bindingResult, Model model)
            throws IOException, InterruptedException {

        var token = request.getSession().getAttribute("token").toString();
        List<String> errorMessages = new ArrayList<>();

        var check = vendorRestService.checkInfo(token);
        model.addAttribute("vendorInfo", check);

        var user = userRestService.viewProfile(new LoginJwtResponseDTO(token));
        model.addAttribute("vendor", user);

        if (bindingResult.hasErrors()){
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            model.addAttribute("error", errorMessages);

            var listBerkasExisting = berkasRestService.retrieveAllBerkas(token);
            model.addAttribute("listBerkasExisting", listBerkasExisting);

            UpdateBerkasFormDTO berkasFormDTO = new UpdateBerkasFormDTO();
            List<UpdateBerkasDTO> listBerkasInDTO = new ArrayList<>();

            for (BerkasInfoResponseDTO berkasInfo : listBerkasExisting) {
                UpdateBerkasDTO berkas = new UpdateBerkasDTO();
                berkas.setId(berkasInfo.getId());
                berkas.setNama(berkasInfo.getNama());
                berkas.setDeskripsi(berkasInfo.getDeskripsi());
                listBerkasInDTO.add(berkas);
            }

            berkasFormDTO.setListBerkas(listBerkasInDTO);
            model.addAttribute("berkasFormDTO", berkasFormDTO);

            return "form-va-edit.html";
        }

        for (UpdateBerkasDTO berkasDTO: updateBerkasFormDTO.getListBerkas()) {

            var updateBerkasDTO = new UpdateBerkasRequestDTO();
            updateBerkasDTO.setId(berkasDTO.getId());
            updateBerkasDTO.setNama(berkasDTO.getNama());
            updateBerkasDTO.setDeskripsi(berkasDTO.getDeskripsi());
            updateBerkasDTO.setJudul(berkasDTO.getFile().getOriginalFilename());
            updateBerkasDTO.setType(berkasDTO.getFile().getContentType());
            updateBerkasDTO.setData(berkasDTO.getFile().getBytes());
            updateBerkasDTO.setSize(berkasDTO.getFile().getSize());
            updateBerkasDTO.setToken(token);

            var output = berkasRestService.updateBerkas(token, updateBerkasDTO);

            if (output.containsKey("error")) {
                errorMessages.add(output.get("error"));
                model.addAttribute("error", errorMessages);

                var listBerkasExisting = berkasRestService.retrieveAllBerkas(token);
                model.addAttribute("listBerkasExisting", listBerkasExisting);

                UpdateBerkasFormDTO berkasFormDTO = new UpdateBerkasFormDTO();
                List<UpdateBerkasDTO> listBerkasInDTO = new ArrayList<>();

                for (BerkasInfoResponseDTO berkasInfo : listBerkasExisting) {
                    UpdateBerkasDTO berkas = new UpdateBerkasDTO();
                    berkas.setId(berkasInfo.getId());
                    berkas.setNama(berkasInfo.getNama());
                    berkas.setDeskripsi(berkasInfo.getDeskripsi());
                    listBerkasInDTO.add(berkas);
                }

                berkasFormDTO.setListBerkas(listBerkasInDTO);
                model.addAttribute("berkasFormDTO", berkasFormDTO);

                return "form-va-edit.html";
            }
        }
        var submissionChange = vendorRestService.toggleSubmission(token);
        return "redirect:/vendor-assessment";
    }

}