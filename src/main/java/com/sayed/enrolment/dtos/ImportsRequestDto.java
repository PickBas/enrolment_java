package com.sayed.enrolment.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ImportsRequestDto {
    private List<EntityDto> items;
    private Timestamp updateDate;
}
