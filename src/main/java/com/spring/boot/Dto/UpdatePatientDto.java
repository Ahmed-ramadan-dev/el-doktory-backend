package com.spring.boot.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "تمثل البيانات المطلوبة لتحديث معلومات مريض موجود")
public class UpdatePatientDto {

    @NotBlank(message = "رقم المريض مطلوب")
    @Schema(description = "المعرف الفريد للمريض", example = "ABCD1234", required = true)
    private String id;

    @NotBlank(message = "الاسم مطلوب")
    @Schema(description = "اسم المريض الكامل", example = "أحمد رمضان", required = true)
    private String name;

    @Min(value = 1, message = "العمر يجب أن يكون أكبر من صفر")
    @Max(value = 120, message = "العمر يجب أن يكون أقل من أو يساوي 120")
    @Schema(description = "عمر المريض", example = "30", required = true)
    private int age;

    @NotBlank(message = "رقم التليفون مطلوب")
    @Pattern(regexp = "^0\\d{10}$", message = "رقم التليفون يجب أن يبدأ بصفر ويكون 11 رقم")
    @Schema(description = "رقم التليفون الخاص بالمريض", example = "01012345678", required = true)
    private String phoneNumber;

    @NotBlank(message = "العنوان مطلوب")
    @Schema(description = "عنوان المريض", example = "شارع التحرير، القاهرة", required = true)
    private String address;
}
