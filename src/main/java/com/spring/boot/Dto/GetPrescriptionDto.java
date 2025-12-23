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
@Schema(description = "تمثل بيانات الوصفة الطبية التي يتم عرضها عند جلب وصفة واحدة")
public class GetPrescriptionDto {

    @Schema(description = "المعرف الفريد للوصفة الطبية", example = "1")
    private Long id;

    @Schema(description = "اسم الوصفة أو عنوانها", example = "وصفة علاج ارتفاع ضغط الدم")
    private String name;

    @Schema(description = "ملاحظات إضافية على الوصفة", example = "يتم تناول الدواء بعد الطعام")
    private String note;

    @Schema(description = "اسم الطبيب الذي أصدر الوصفة", example = "د. أحمد رمضان")
    private String doctorName;

    @Schema(description = "تخصص الطبيب الذي أصدر الوصفة", example = "طب الباطنة")
    private String doctorSpecialty;

    @Schema(description = "قائمة روابط الملفات المرفوعة للوصفة")
    private List<String> fileUrls;
}
