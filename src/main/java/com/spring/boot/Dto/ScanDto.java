package com.spring.boot.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "تمثل بيانات الفحص أو الأشعة التي يتم عرضها في الاستجابة مع تفاصيل الطبيب والملفات والتواريخ")
public class ScanDto {

    @Schema(description = "المعرف الفريد للفحص", example = "1")
    private Long id;

    @Schema(description = "اسم الفحص أو الأشعة", example = "أشعة صدر")
    private String name;

    @Schema(description = "قائمة روابط الملفات المرفوعة للفحص")
    private List<String> fileUrls = new ArrayList<>();

    @Schema(description = "ملاحظات إضافية على الفحص", example = "المريض يجب أن يحضر بدون طعام لمدة 6 ساعات")
    private String note;

    @Schema(description = "اسم الطبيب الذي أجرى الفحص", example = "د. أحمد رمضان")
    private String doctorName;

    @Schema(description = "تخصص الطبيب الذي أجرى الفحص", example = "طب الباطنة")
    private String doctorSpecialty;

    @Schema(description = "تاريخ إنشاء الفحص", example = "2025-12-21T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "تاريخ آخر تعديل للفحص", example = "2025-12-21T10:20:30")
    private LocalDateTime updatedAt;
}
