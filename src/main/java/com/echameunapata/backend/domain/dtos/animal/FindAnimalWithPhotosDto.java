package com.echameunapata.backend.domain.dtos.animal;

import com.echameunapata.backend.domain.dtos.image.FindImagesDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindAnimalWithPhotosDto extends FindAnimalDto {
    private List<FindImagesDto> photos;
}

