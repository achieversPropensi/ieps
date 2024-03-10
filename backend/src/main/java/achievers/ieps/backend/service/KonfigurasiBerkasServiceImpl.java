package achievers.ieps.backend.service;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
        var listNoDeleted = konfigurasiBerkasDb.findAll();        
        Iterator<KonfigurasiBerkas> iterator = listNoDeleted.iterator();
        while(iterator.hasNext()){
            KonfigurasiBerkas konfigurasiBerkas = iterator.next();
            if(konfigurasiBerkas.isDeleted()){
                iterator.remove(); 
            }
        }
        return listNoDeleted; 
    }
    

    @Override
    public List<KonfigurasiBerkas> getAllKonfigurasiBerkas2(){
        return konfigurasiBerkasDb.findAll();
    }

    @Override
    public ResponseEntity<String> addKonfigurasiBerkas(List<KonfigurasiBerkas> listKonfigurasiBerkas) {
        List<KonfigurasiBerkas> listKB = getAllKonfigurasiBerkas();

        // Handle input kososng
        if (listKonfigurasiBerkas.isEmpty() || listKonfigurasiBerkas == null){
            for (KonfigurasiBerkas kb : listKB) {
                kb.setDeleted(true);
            }
            return ResponseEntity.ok("Konfigurasi berkas berhasil ditambahkan.");
        }
        

        // Handle berkas hanya ada 5
        if (listKonfigurasiBerkas.size() == 5){
            return ResponseEntity.badRequest().body("Maksimum 5 konfigurasi berkas");
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
        
        // Handle soft delete
        for (KonfigurasiBerkas kb : listKB) {
            if (listKonfigurasiBerkas.contains(kb)) {
                listKonfigurasiBerkas.remove(kb);
            } else {
                kb.setDeleted(true);
            }
        }

        // Save
        for (KonfigurasiBerkas kb : listKonfigurasiBerkas) {
            String berkasId = kb.getNamaBerkas().replace(" ", "-").toLowerCase();
            kb.setBerkasId(berkasId);
            
            String[] words = kb.getNamaBerkas().split("\\s+");
            StringBuilder result = new StringBuilder();
    
            for (String word : words) {
                if (!word.isEmpty()) {
                    result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
                }
            }
    
            String nb =  result.toString().trim();
            kb.setNamaBerkas(nb);
            
            konfigurasiBerkasDb.save(kb);
        }
        return ResponseEntity.ok("Konfigurasi berkas berhasil ditambahkan.");
    }

}
