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
@Schema(description = "تمثل البيانات المطلوبة لإنشاء وصفة طبية جديدة للمريض")
public class CreatePrescriptionDto {

    @NotBlank(message = "اسم الوصفة مطلوب")
    @Size(max = 100, message = "اسم الوصفة لا يجب أن يزيد عن 100 حرف")
    @Schema(description = "اسم الوصفة أو عنوانها", example = "وصفة علاج ارتفاع ضغط الدم", required = true)
    private String name;

    @Size(max = 500, message = "الملاحظات لا يجب أن تزيد عن 500 حرف")
    @Schema(description = "ملاحظات إضافية على الوصفة", example = "يتم تناول الدواء بعد الطعام")
    private String note;

    @NotBlank(message = "اسم الدكتور مطلوب")
    @Size(max = 100, message = "اسم الدكتور لا يجب أن يزيد عن 100 حرف")
    @Schema(description = "اسم الطبيب الذي سيصدر الوصفة", example = "د. أحمد رمضان", required = true)
    private String doctorName;

    @NotNull(message = "يجب اختيار تخصص الطبيب")
    @Schema(description = "معرف تخصص الطبيب", example = "2", required = true)
    private Long specialtyId;
}
