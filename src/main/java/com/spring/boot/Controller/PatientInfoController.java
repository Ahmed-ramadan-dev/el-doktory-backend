package com.spring.boot.Controller;

import com.spring.boot.Dto.PatientInfoDto;
import com.spring.boot.Service.PatientInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/patient/info")
public class PatientInfoController {

    private final PatientInfoService patientInfoService;

    public PatientInfoController(PatientInfoService patientInfoService) {
        this.patientInfoService = patientInfoService;
    }

    @GetMapping("/{patientId}")
    @Operation(
            summary = "جلب معلومات المريض للعرض في الفرونت",
            description = "يعرض معلومات المريض الأساسية مثل الاسم، العمر، رقم الهاتف والعنوان",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم جلب معلومات المريض بنجاح",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PatientInfoDto.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "المريض غير موجود",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            }
    )
    public ResponseEntity<PatientInfoDto> getPatientInfo(
            @Parameter(description = "معرف المريض") @PathVariable String patientId
    ) {
        return ResponseEntity.ok(patientInfoService.getPatientInfo(patientId));
    }
}
