package achievers.ieps.backend.restcontroller;

import achievers.ieps.backend.dto.request.CreateBerkasRequestDTO;
import achievers.ieps.backend.dto.request.UpdateBerkasRequestDTO;
import achievers.ieps.backend.dto.response.BerkasResponseDTO;
import achievers.ieps.backend.model.Berkas;
import achievers.ieps.backend.service.BerkasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/berkas")
public class BerkasRestController {
    private static final String ERRORSTATUS = "error";
    private static final String SUCCESSSTATUS = "success";

    @Autowired
    private BerkasService berkasService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestBody CreateBerkasRequestDTO createBerkasRequestDTO) {
        Map<String, String> response = new HashMap<>();
        System.out.println(createBerkasRequestDTO.getData().length);
        if (createBerkasRequestDTO.getData().length == 0) {
            berkasService.deleteBerkasVendor(createBerkasRequestDTO.getToken());
            response.put(ERRORSTATUS, "Berkas "+ createBerkasRequestDTO.getNama() + " tidak boleh kosong!");
            return ResponseEntity.ok().body(response);
        }
        if (!Objects.equals(createBerkasRequestDTO.getType(), MediaType.APPLICATION_PDF_VALUE)) {
            berkasService.deleteBerkasVendor(createBerkasRequestDTO.getToken());
            response.put(ERRORSTATUS, "Mohon unggah kembali berkas "+ createBerkasRequestDTO.getNama() + " dalam format PDF!");
            return ResponseEntity.ok().body(response);
        }
        try {
            berkasService.store(createBerkasRequestDTO);
            response.put(SUCCESSSTATUS, "Berkas berhasil diunggah: " + createBerkasRequestDTO.getJudul());
            System.out.println("Berkas berhasil diunggah: " + createBerkasRequestDTO.getNama());
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Berkas belum berhasil diunggah: " + createBerkasRequestDTO.getNama());
            berkasService.deleteBerkasVendor(createBerkasRequestDTO.getToken());
            response.put(ERRORSTATUS, "Berkas belum berhasil diunggah: " + createBerkasRequestDTO.getJudul());
            return ResponseEntity.ok().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<BerkasResponseDTO>> getBerkasVendor(@PathVariable String id) {
        List<BerkasResponseDTO> files = berkasService.getAllBerkas(id).stream().map(berkas -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/berkas/")
                    .path(id + "/")
                    .path(berkas.getId().toString())
                    .toUriString();

            return new BerkasResponseDTO(
                    berkas.getId(),
                    berkas.getNama(),
                    berkas.getDeskripsi(),
                    berkas.getJudul(),
                    fileDownloadUri,
                    berkas.getType());
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/{vendorId}/{berkasId}")
    public ResponseEntity<byte[]> getBerkas(
            @PathVariable("vendorId") String vendorId,
            @PathVariable("berkasId") String berkasId) {

        Berkas berkas = berkasService.getBerkas(UUID.fromString(vendorId), Long.valueOf(berkasId));

        if (berkas != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(berkas.getJudul()).build());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(berkas.getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, String>> updateFile(
            @RequestBody UpdateBerkasRequestDTO updateBerkasRequestDTO) {
        Map<String, String> response = new HashMap<>();

        if (updateBerkasRequestDTO.getData().length == 0) {
            response.put(ERRORSTATUS, "Berkas "+ updateBerkasRequestDTO.getNama() + " tidak boleh kosong!");
            return ResponseEntity.ok().body(response);
        }
        if (!Objects.equals(updateBerkasRequestDTO.getType(), MediaType.APPLICATION_PDF_VALUE)) {
            response.put(ERRORSTATUS, "Mohon unggah kembali berkas "+ updateBerkasRequestDTO.getNama() + " dalam format PDF!");
            return ResponseEntity.ok().body(response);
        }
        try {
            berkasService.edit(updateBerkasRequestDTO);
            response.put(SUCCESSSTATUS, "Berkas berhasil diunggah: " + updateBerkasRequestDTO.getNama());
            System.out.println("Berkas berhasil diunggah: " + updateBerkasRequestDTO.getNama());
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Berkas belum berhasil diunggah: " + updateBerkasRequestDTO.getNama());
            response.put(ERRORSTATUS, "Berkas belum berhasil diunggah: " + updateBerkasRequestDTO.getNama());
            return ResponseEntity.ok().body(response);
        }
    }

}
