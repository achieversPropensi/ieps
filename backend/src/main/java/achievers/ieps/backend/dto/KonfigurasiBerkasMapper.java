package achievers.ieps.backend.dto;

import java.util.List;

import org.mapstruct.Mapper;

import achievers.ieps.backend.dto.request.CreateKonfigurasiBerkasRequestDTO;
import achievers.ieps.backend.dto.response.KonfigurasiBerkasResponseDTO;
import achievers.ieps.backend.model.KonfigurasiBerkas;

@Mapper(componentModel = "spring")
public interface KonfigurasiBerkasMapper {
    KonfigurasiBerkas createKonfigurasiBerkasRequestDTOToKonfigurasiBerkas(CreateKonfigurasiBerkasRequestDTO list);
    List<KonfigurasiBerkasResponseDTO> konfigurasiBerkasToKonfigurasiBerkasResponseDTO(List<KonfigurasiBerkas> konfigurasiBerkas);
}
