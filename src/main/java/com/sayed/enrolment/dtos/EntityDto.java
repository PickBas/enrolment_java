package com.sayed.enrolment.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EntityDto {
    private String id;
    private String url;
    private String parentId;
    private Integer size;
    private String type;
}
