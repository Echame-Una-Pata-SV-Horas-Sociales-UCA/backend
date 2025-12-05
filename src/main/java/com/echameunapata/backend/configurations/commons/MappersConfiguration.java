package com.echameunapata.backend.configurations.commons;

import com.echameunapata.backend.domain.models.Animal;
import com.echameunapata.backend.domain.models.AnimalPhoto;
import com.echameunapata.backend.domain.dtos.animal.FindAnimalWithPhotosDto;
import com.echameunapata.backend.domain.dtos.image.FindImagesDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

@Configuration
public class MappersConfiguration {

    @Bean
    ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();
        TypeMap<Animal, FindAnimalWithPhotosDto> typeMap = mapper.createTypeMap(Animal.class, FindAnimalWithPhotosDto.class);
        typeMap.addMappings(m -> m.skip(FindAnimalWithPhotosDto::setPhotos));
        typeMap.setPostConverter(context -> {
            Animal source = context.getSource();
            FindAnimalWithPhotosDto dest = context.getDestination();
            if (source.getPhotos() != null) {
                dest.setPhotos(
                    source.getPhotos().stream()
                        .map(photo -> {
                            FindImagesDto dto = new FindImagesDto();
                            dto.setUrl(photo.getUrl());
                            dto.setIsPrimary(photo.getIsPrimary());
                            dto.setCreatedAt(photo.getCreatedAt());
                            dto.setContentType(photo.getContentType());
                            return dto;
                        })
                        .collect(Collectors.toList())
                );
            }
            return dest;
        });
        return mapper;
    }
}
