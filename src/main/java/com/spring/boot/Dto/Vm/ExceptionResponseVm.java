package com.spring.boot.Dto.Vm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "تمثل رد الاستجابة عند حدوث استثناء مع الرسائل وحالة HTTP والتاريخ")
public class ExceptionResponseVm {

    @Schema(description = "قائمة الرسائل المتعلقة بالخطأ", example = "[\"حدث خطأ في البيانات\"]")
    private List<String> messages;

    @Schema(description = "حالة HTTP المرتبطة بالاستثناء", example = "BAD_REQUEST")
    private HttpStatus httpStatus;

    @Schema(description = "تاريخ الاستجابة", example = "2025-12-21")
    private LocalDate localDate;

    public ExceptionResponseVm(List<String> messages, HttpStatus httpStatus) {
        this.messages = messages;
        this.httpStatus = httpStatus;
        this.localDate = LocalDate.now();
    }
}
