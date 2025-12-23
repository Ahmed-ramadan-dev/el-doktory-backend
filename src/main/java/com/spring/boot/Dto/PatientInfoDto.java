package com.spring.boot.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "بيانات المريض للعرض في الفرونت")
public class PatientInfoDto {

    @Schema(description = "اسم المريض", example = "أحمد رمضان")
    private String name;

    @Schema(description = "عمر المريض", example = "30")
    private int age;

    @Schema(description = "رقم الهاتف", example = "01012345678")
    private String phoneNumber;

    @Schema(description = "عنوان المريض", example = "القاهرة، مصر")
    private String address;
}
