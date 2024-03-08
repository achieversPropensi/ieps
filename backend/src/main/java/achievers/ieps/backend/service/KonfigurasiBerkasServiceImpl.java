package achievers.ieps.backend.service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import achievers.ieps.backend.model.KonfigurasiBerkas;
import achievers.ieps.backend.repository.KonfigurasiBerkasDb;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class KonfigurasiBerkasServiceImpl implements KonfigurasiBerkasService{
    @Autowired
    KonfigurasiBerkasDb konfigurasiBerkasDb;

    @Override
    public List<KonfigurasiBerkas> getAllKonfigurasiBerkas(){
        return konfigurasiBerkasDb.findAll();
    }

    @Override
    public ResponseEntity<String> addKonfigurasiBerkas(List<KonfigurasiBerkas> listKonfigurasiBerkas) {
        List<KonfigurasiBerkas> listKB = getAllKonfigurasiBerkas();

        // Handle input kososng
        if (listKonfigurasiBerkas.isEmpty()){
            return ResponseEntity.badRequest().body("Tidak boleh kosong");
        }

        // Handle input namaBerkas dan Nama Berkas sama
        Set<String> namaBerkasSet = new HashSet<>();
        for (KonfigurasiBerkas kb : listKonfigurasiBerkas){
            if (kb.getNamaBerkas() == null || kb.getNamaBerkas().isBlank()){
                return ResponseEntity.badRequest().body("Nama berkas tidak boleh kosong.");
            } 
            if (namaBerkasSet.contains(kb.getNamaBerkas())) {
                return ResponseEntity.badRequest().body("Nama berkas tidak boleh sama.");
            }
            namaBerkasSet.add(kb.getNamaBerkas());
        }
        
        for (KonfigurasiBerkas kb : listKB) {
            if (listKonfigurasiBerkas.contains(kb)) {
                listKonfigurasiBerkas.remove(kb);
            } else {
                kb.setDeleted(true);
            }
        }

        for (KonfigurasiBerkas kb : listKonfigurasiBerkas) {
            konfigurasiBerkasDb.save(kb);
        }
        return ResponseEntity.ok("Konfigurasi berkas berhasil ditambahkan.");
    }

}
