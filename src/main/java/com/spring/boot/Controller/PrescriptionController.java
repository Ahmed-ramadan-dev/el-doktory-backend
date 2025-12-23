package com.spring.boot.Controller;

import com.spring.boot.Dto.CreatePrescriptionDto;
import com.spring.boot.Dto.GetPrescriptionDto;
import com.spring.boot.Dto.PrescriptionDto;
import com.spring.boot.Dto.Vm.ExceptionResponseVm;
import com.spring.boot.Service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/prescription/")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/patient/{patientId}")
    @Operation(
            summary = "جلب كل الوصفات لمريض",
            description = "يعرض كل الوصفات الخاصة بالمريض",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم جلب الوصفات بنجاح",
                            content = @Content(schema = @Schema(implementation = PrescriptionDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "المريض غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<List<PrescriptionDto>> getAllPrescriptions(
            @PathVariable @Parameter(description = "معرف المريض", example = "ABCD1234") String patientId) {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions(patientId));
    }

    @GetMapping("/patient/{patientId}/search")
    @Operation(
            summary = "البحث في الوصفات",
            description = "يمكن البحث بالاسم أو بالتاريخ",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم البحث بنجاح",
                            content = @Content(schema = @Schema(implementation = PrescriptionDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "المريض غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<List<PrescriptionDto>> searchPrescriptions(
            @PathVariable @Parameter(description = "معرف المريض", example = "ABCD1234") String patientId,
            @RequestParam(required = false) @Parameter(description = "كلمة البحث في الاسم", example = "Painkiller") String keyword,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "تاريخ البحث", example = "2025-12-21") LocalDate date) {
        return ResponseEntity.ok(prescriptionService.searchPrescriptions(patientId, keyword, date));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "جلب وصفة واحدة",
            description = "يعرض تفاصيل وصفة محددة",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم جلب الوصفة بنجاح",
                            content = @Content(schema = @Schema(implementation = GetPrescriptionDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "الوصفة غير موجودة",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<GetPrescriptionDto> getPrescriptionById(
            @PathVariable @Parameter(description = "معرف الوصفة", example = "1") Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @PostMapping("/patient/{patientId}")
    @Operation(
            summary = "رفع وصفة جديدة",
            description = "يمكن رفع ملفات متعددة مع بيانات الوصفة",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم رفع الوصفة بنجاح"),
                    @ApiResponse(responseCode = "404",
                            description = "المريض أو التخصص غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class))),
                    @ApiResponse(responseCode = "400",
                            description = "الدكتور موجود بنفس الاسم والتخصص أو الملفات فارغة",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<String> uploadPrescription(
            @PathVariable @Parameter(description = "معرف المريض", example = "ABCD1234") String patientId,
            @RequestParam("files") @Parameter(description = "ملفات الوصفة") MultipartFile[] files,
            @Valid @ModelAttribute CreatePrescriptionDto dto) {
        prescriptionService.uploadPrescription(patientId, files, dto);
        return ResponseEntity.ok("تم رفع الوصفة بنجاح");
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "تحديث وصفة موجودة",
            description = "تحديث بيانات الوصفة ورفع ملفات جديدة أو حذف ملفات موجودة",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم تحديث الوصفة بنجاح"),
                    @ApiResponse(responseCode = "404",
                            description = "المريض أو الوصفة أو التخصص غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class))),
                    @ApiResponse(responseCode = "400",
                            description = "الدكتور موجود بنفس الاسم والتخصص أو الملفات غير صحيحة",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<String> updatePrescription(
            @PathVariable @Parameter(description = "معرف الوصفة", example = "1") Long id,
            @RequestParam(value = "files", required = false) @Parameter(description = "ملفات جديدة") MultipartFile[] files,
            @ModelAttribute @Valid CreatePrescriptionDto dto,
            @RequestParam(value = "filesToDelete", required = false) @Parameter(description = "ملفات للحذف") List<String> filesToDelete) {
        prescriptionService.updatePrescription(id, files, dto, filesToDelete);
        return ResponseEntity.ok("تم تحديث الوصفة بنجاح");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "حذف وصفة",
            description = "يحذف الوصفة المحددة",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم حذف الوصفة بنجاح"),
                    @ApiResponse(responseCode = "404",
                            description = "الوصفة غير موجودة",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<String> deletePrescription(
            @PathVariable @Parameter(description = "معرف الوصفة", example = "1") Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.ok("تم حذف الوصفة بنجاح");
    }
}
