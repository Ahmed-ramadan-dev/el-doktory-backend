package com.spring.boot.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "تمثل بيانات الطبيب التي يتم عرضها في الاستجابة")
public class DoctorDto {

    @Schema(description = "المعرف الفريد للطبيب", example = "1")
    private Long id;

    @Schema(description = "اسم الطبيب الكامل", example = "د. أحمد رمضان")
    private String name;

    @Schema(description = "اسم التخصص الطبي للطبيب", example = "طب الباطنة")
    private String specialtyName;
}
