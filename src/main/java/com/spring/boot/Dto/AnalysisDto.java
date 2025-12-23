package com.spring.boot.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "تمثل بيانات التحليل المرسلة للعميل مع معلومات الطبيب والملفات والتواريخ")
public class AnalysisDto {

    @Schema(description = "المعرف الفريد للتحليل", example = "1")
    private Long id;

    @Schema(description = "اسم التحليل", example = "تحاليل دم")
    private String name;

    @Schema(description = "قائمة روابط الملفات المرفوعة للتحليل")
    private List<String> fileUrls = new ArrayList<>();

    @Schema(description = "ملاحظات إضافية على التحليل", example = "المريض يجب أن يصوم 12 ساعة قبل التحليل")
    private String note;

    @Schema(description = "اسم الطبيب الذي أجرى التحليل", example = "د. أحمد رمضان")
    private String doctorName;

    @Schema(description = "تخصص الطبيب الذي أجرى التحليل", example = "طب الباطنة")
    private String doctorSpecialty;

    @Schema(description = "تاريخ إنشاء التحليل", example = "2025-12-21T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "تاريخ آخر تحديث للتحليل", example = "2025-12-21T10:20:30")
    private LocalDateTime updatedAt;
}
