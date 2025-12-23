package com.spring.boot.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "تمثل البيانات المطلوبة لإنشاء فحص أو أشعة جديدة للمريض")
public class CreateScanDto {

    @NotBlank(message = "اسم الأشعة مطلوب")
    @Size(max = 100, message = "اسم الأشعة لا يجب أن يزيد عن 100 حرف")
    @Schema(description = "اسم الأشعة أو الفحص", example = "أشعة صدر", required = true)
    private String name;

    @Size(max = 500, message = "الملاحظات لا يجب أن تزيد عن 500 حرف")
    @Schema(description = "ملاحظات إضافية على الفحص", example = "المريض يجب أن يحضر بدون طعام لمدة 6 ساعات")
    private String note;

    @NotBlank(message = "اسم الدكتور مطلوب")
    @Size(max = 100, message = "اسم الدكتور لا يجب أن يزيد عن 100 حرف")
    @Schema(description = "اسم الطبيب الذي سيجري الفحص", example = "د. أحمد رمضان", required = true)
    private String doctorName;

    @NotNull(message = "يجب اختيار تخصص الطبيب")
    @Schema(description = "معرف تخصص الطبيب", example = "2", required = true)
    private Long specialtyId;
}
