package com.spring.boot.Controller;

import com.spring.boot.Dto.CreatePatientDto;
import com.spring.boot.Dto.UpdatePatientDto;
import com.spring.boot.Dto.Vm.ExceptionResponseVm;
import com.spring.boot.Service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/api/eldoctory/safepatient/add")
    @Operation(
            summary = "إضافة مريض جديد",
            description = "ينشئ مريض جديد ويولد له QR code",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "تم إنشاء المريض بنجاح",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "400",
                            description = "بيانات المريض غير صحيحة",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<Map<String, String>> createPatient(
            @Valid @RequestBody @Parameter(description = "بيانات المريض الجديد") CreatePatientDto dto) {
        Map<String, String> response = patientService.createPatientWithQR(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/api/eldoctory/safepatient/update")
    @Operation(
            summary = "تحديث بيانات مريض",
            description = "يتم تحديث بيانات المريض باستخدام المعرف",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم تحديث بيانات المريض بنجاح"),
                    @ApiResponse(responseCode = "400",
                            description = "بيانات المريض غير صحيحة",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class))),
                    @ApiResponse(responseCode = "404",
                            description = "المريض غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<String> updatePatient(
            @Valid @RequestBody @Parameter(description = "بيانات التحديث للمريض") UpdatePatientDto updatePatientDto) {
        patientService.updatePatient(updatePatientDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body("تم تحديث بيانات المريض بنجاح");
    }

    @DeleteMapping("/api/eldoctory/safepatient/delete/{id}")
    @Operation(
            summary = "حذف مريض",
            description = "يحذف المريض المحدد بواسطة المعرف",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم حذف المريض بنجاح"),
                    @ApiResponse(responseCode = "404",
                            description = "المريض غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<String> deletePatient(
            @PathVariable @Parameter(description = "معرف المريض", example = "ABCD1234") String id
    ) {
        patientService.deletePatient(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("تم حذف المريض بنجاح");
    }

    @GetMapping("/api/eldoctory/safepatient/getAll/")
    @Operation(
            summary = "جلب كل المرضى",
            description = "يعرض قائمة بكل المرضى الموجودين",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم جلب قائمة المرضى بنجاح",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UpdatePatientDto.class)))
            }
    )
    public ResponseEntity<List<UpdatePatientDto>> getPatientById() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(patientService.getPatient());
    }
}
