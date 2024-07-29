package com.glerk.core.dto;

import com.glerk.core.entity.Template;
import lombok.Data;

@Data
public class HashtagDto {
    private Long id;
    private String name;
    private String hashtag;

    public static HashtagDto fromEntity(Template template) {
        HashtagDto hashtagDto = new HashtagDto();
        hashtagDto.setId(template.getId());
        hashtagDto.setName(template.getName());
        hashtagDto.setHashtag(template.getHashtag());
        return hashtagDto;
    }
}
