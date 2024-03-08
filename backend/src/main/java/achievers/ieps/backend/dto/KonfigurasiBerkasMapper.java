package achievers.ieps.backend.dto;

import org.mapstruct.Mapper;

import achievers.ieps.backend.dto.request.CreateKonfigurasiBerkasRequestDTO;
import achievers.ieps.backend.model.KonfigurasiBerkas;

@Mapper(componentModel = "spring")
public interface KonfigurasiBerkasMapper {
    KonfigurasiBerkas createKonfigurasiBerkasRequestDTOToKonfigurasiBerkas(CreateKonfigurasiBerkasRequestDTO createKonfigurasiBerkasRequestDTO);
}
