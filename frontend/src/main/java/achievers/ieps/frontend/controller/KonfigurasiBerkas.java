package achievers.ieps.frontend.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import achievers.ieps.frontend.dto.request.CreateKonfigurasiBerkasRequestDTO;
import achievers.ieps.frontend.dto.response.KonfigurasiBerkasResponseDTO;
import achievers.ieps.frontend.dto.response.KonfigurasiBerkasWrapperRespondeDTO;
import achievers.ieps.frontend.restservice.KonfigurasiBerkasRestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class KonfigurasiBerkas {
    
    @Autowired
    KonfigurasiBerkasRestService konfigurasiBerkasRestService;

    @GetMapping("/konfigurasi-berkas")
    public String retrieveKonfigurasiBerkas(HttpServletRequest httprequest, Model model) throws JSONException{
        var token = httprequest.getSession().getAttribute("token").toString();
        List<KonfigurasiBerkasResponseDTO> lst = konfigurasiBerkasRestService.getAllKonfigurasiBerkas(token);
        model.addAttribute("lst", lst);
        
        return "konfigurasi-berkas";
    }

    @GetMapping("/konfigurasi-berkas/edit")
    public String editKonfigurasiBerkas(HttpServletRequest httprequest, Model model) throws JSONException{
        var token = httprequest.getSession().getAttribute("token").toString();
    
        List<KonfigurasiBerkasResponseDTO> lst = konfigurasiBerkasRestService.getAllKonfigurasiBerkas(token);
        List<CreateKonfigurasiBerkasRequestDTO> lstNew = new ArrayList<>();

        // Kalau kosong
        if(lst == null || lst.size() == 0){
            CreateKonfigurasiBerkasRequestDTO createNew = new CreateKonfigurasiBerkasRequestDTO();
            createNew.setNamaBerkas("");
            createNew.setDeskripsi("");
            lstNew.add(createNew);
            
        } else {
            for (KonfigurasiBerkasResponseDTO i : lst){
                CreateKonfigurasiBerkasRequestDTO createNew = new CreateKonfigurasiBerkasRequestDTO();
                createNew.setNamaBerkas(i.getNamaBerkas());
                createNew.setDeskripsi(i.getDeskripsi());
                lstNew.add(createNew);
            }
        }
        
        KonfigurasiBerkasWrapperRespondeDTO kbrequest = new KonfigurasiBerkasWrapperRespondeDTO();
        kbrequest.setListKB(lstNew);
        model.addAttribute("kbrequest", kbrequest);
        return "konfigurasi-berkas-form";
    }

    @PostMapping("/konfigurasi-berkas/edit")
    public String saveKonfigurasiBerkas(@ModelAttribute KonfigurasiBerkasWrapperRespondeDTO kbrequest, RedirectAttributes redirectAttributes, HttpServletRequest httprequest, Model model, BindingResult bindingResult) throws JSONException {

        // Handling nama sama
        if(kbrequest.getListKB() != null){
            Set<String> namaBerkasSet = new HashSet<>();
            Set<String> errorMessages = new HashSet<>();
            for (CreateKonfigurasiBerkasRequestDTO dto : kbrequest.getListKB()){
                if (namaBerkasSet.contains(dto.getNamaBerkas().toLowerCase())) {
                    errorMessages.add("Nama berkas tidak boleh sama");
                }
                if (dto.getNamaBerkas() == null || dto.getNamaBerkas().isEmpty()){
                    errorMessages.add("Nama berkas tidak boleh kosong");
                }
                if (dto.getDeskripsi() == null || dto.getDeskripsi().isEmpty()){
                    errorMessages.add("Deskripsi berkas tidak boleh kosong");
                }
                if (dto.getDeskripsi().length() >= 200){
                    errorMessages.add("Deskripsi lebih dari 200 kata");
                }
                namaBerkasSet.add(dto.getNamaBerkas().toLowerCase());
            }
            if(!errorMessages.isEmpty()){
                model.addAttribute("errorMessages", errorMessages);
                model.addAttribute("kbrequest", kbrequest);
                return "konfigurasi-berkas-form";
            }
        }
        
        var token = httprequest.getSession().getAttribute("token").toString();
        konfigurasiBerkasRestService.addKonfigurasiBerkas(kbrequest.getListKB(), token);
        List<KonfigurasiBerkasResponseDTO> lst = konfigurasiBerkasRestService.getAllKonfigurasiBerkas(token);
        konfigurasiBerkasRestService.addKonfigurasiBerkas(kbrequest.getListKB(), token);
        redirectAttributes.addFlashAttribute("successMessage", "Konfigurasi berkas Anda berhasil disimpan.");
        return "redirect:/konfigurasi-berkas";
    }

    @PostMapping(value="/konfigurasi-berkas/edit", params ={"addRow"})
    public String addRowKonfigurasiBerkas(@ModelAttribute KonfigurasiBerkasWrapperRespondeDTO kbrequest, HttpServletRequest httprequest, Model model) throws JSONException{
        var token = httprequest.getSession().getAttribute("token").toString();

        // Kalau kosong
        if(kbrequest.getListKB() == null || kbrequest.getListKB().size() == 0){
            kbrequest.setListKB(new ArrayList<>());
        }
        // Maksimum 5 file
        if (kbrequest.getListKB().size() == 5){
            model.addAttribute("kbrequest", kbrequest);
            return "konfigurasi-berkas-form";
        }

        // Memasukan CreateKonfigurasiBerkasRequestDTO baru (kosong) ke list
        kbrequest.getListKB().add(new CreateKonfigurasiBerkasRequestDTO());
        model.addAttribute("kbrequest", kbrequest);
        return "konfigurasi-berkas-form";
        
    }

    @PostMapping(value="/konfigurasi-berkas/edit", params ={"deleteRow"})
    public String deleteRowKonfigurasiBerkas(@ModelAttribute KonfigurasiBerkasWrapperRespondeDTO kbrequest, @RequestParam("deleteRow") int row, HttpServletRequest httprequest, Model model) throws JSONException{
        var token = httprequest.getSession().getAttribute("token").toString();
        kbrequest.getListKB().remove(row);
        model.addAttribute("kbrequest", kbrequest);
        return "konfigurasi-berkas-form";
    }

}
