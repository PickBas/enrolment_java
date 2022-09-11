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

    public boolean validate() {
        if (id == null) {
            return false;
        }
        if (type.equals("FOLDER") && url != null) {
            return false;
        }
        if (url != null && url.length() > 255) {
            return false;
        }
        if (type.equals("FOLDER") && size != null) {
            return false;
        }
        return true;
    }
}
