package achievers.ieps.frontend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        for (KonfigurasiBerkasResponseDTO i : lst){
            CreateKonfigurasiBerkasRequestDTO createNew = new CreateKonfigurasiBerkasRequestDTO();
            createNew.setNamaBerkas(i.getNamaBerkas());
            lstNew.add(createNew);
        }
        KonfigurasiBerkasWrapperRespondeDTO kbrequest = new KonfigurasiBerkasWrapperRespondeDTO();
        kbrequest.setListKB(lstNew);
        model.addAttribute("kbrequest", kbrequest);
        return "konfigurasi-berkas-form";
    }

    @PostMapping("/konfigurasi-berkas/edit")
    public String saveKonfigurasiBerkas(@Valid @ModelAttribute KonfigurasiBerkasWrapperRespondeDTO kbrequest, HttpServletRequest httprequest, Model model) throws JSONException{
        var token = httprequest.getSession().getAttribute("token").toString();
        konfigurasiBerkasRestService.addKonfigurasiBerkas(kbrequest.getListKB(), token);
        return "konfigurasi-berkas-success";
    }

    @PostMapping(value="/konfigurasi-berkas/edit", params ={"addRow"})
    public String addRowKonfigurasiBerkas(@Valid @ModelAttribute KonfigurasiBerkasWrapperRespondeDTO kbrequest, HttpServletRequest httprequest, Model model) throws JSONException{
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
    public String deleteRowKonfigurasiBerkas(@Valid @ModelAttribute KonfigurasiBerkasWrapperRespondeDTO kbrequest, @RequestParam("deleteRow") int row, HttpServletRequest httprequest, Model model) throws JSONException{
        var token = httprequest.getSession().getAttribute("token").toString();
        kbrequest.getListKB().remove(row);
        model.addAttribute("kbrequest", kbrequest);
        return "konfigurasi-berkas-form";
    }

}
