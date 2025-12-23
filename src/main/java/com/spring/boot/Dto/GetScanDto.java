package com.spring.boot.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "تمثل بيانات الفحص أو الأشعة التي يتم عرضها عند جلب فحص واحد")
public class GetScanDto {

    @Schema(description = "المعرف الفريد للفحص", example = "1")
    private Long id;

    @Schema(description = "اسم الفحص أو الأشعة", example = "أشعة صدر")
    private String name;

    @Schema(description = "ملاحظات إضافية على الفحص", example = "المريض يجب أن يحضر بدون طعام لمدة 6 ساعات")
    private String note;

    @Schema(description = "اسم الطبيب الذي أجرى الفحص", example = "د. أحمد رمضان")
    private String doctorName;

    @Schema(description = "تخصص الطبيب الذي أجرى الفحص", example = "طب الباطنة")
    private String doctorSpecialty;

    @Schema(description = "قائمة روابط الملفات المرفوعة للفحص")
    private List<String> fileUrls;
}
