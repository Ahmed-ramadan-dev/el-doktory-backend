package com.spring.boot.Controller;

import com.spring.boot.Dto.AnalysisDto;
import com.spring.boot.Dto.CreateAnalysisDto;
import com.spring.boot.Dto.GetAnalysisDto;
import com.spring.boot.Dto.Vm.ExceptionResponseVm;
import com.spring.boot.Service.AnalysisService;
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
@RequestMapping("api/analysis/")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/patient/{patientId}")
    @Operation(
            summary = "جلب كل التحاليل لمريض",
            description = "يعرض كل التحاليل الخاصة بالمريض",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم جلب التحاليل بنجاح",
                            content = @Content(schema = @Schema(implementation = AnalysisDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "المريض غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<List<AnalysisDto>> getAllAnalysis(
            @PathVariable @Parameter(description = "معرف المريض", example = "ABCD1234") String patientId) {
        return ResponseEntity.ok(analysisService.getAllAnalysis(patientId));
    }

    @GetMapping("/patient/{patientId}/search")
    @Operation(
            summary = "البحث في التحاليل",
            description = "يمكن البحث بالاسم أو بالتاريخ",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم البحث بنجاح",
                            content = @Content(schema = @Schema(implementation = AnalysisDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "المريض غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<List<AnalysisDto>> searchAnalysis(
            @PathVariable @Parameter(description = "معرف المريض", example = "ABCD1234") String patientId,
            @RequestParam(required = false) @Parameter(description = "كلمة البحث في الاسم", example = "Blood Test") String keyword,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "تاريخ البحث", example = "2025-12-21") LocalDate date) {
        return ResponseEntity.ok(analysisService.searchAnalysis(patientId, keyword, date));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "جلب تحليل واحد",
            description = "يعرض تفاصيل تحليل محدد",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم جلب التحليل بنجاح",
                            content = @Content(schema = @Schema(implementation = GetAnalysisDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "التحليل غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<GetAnalysisDto> getAnalysisById(
            @PathVariable @Parameter(description = "معرف التحليل", example = "1") Long id) {
        return ResponseEntity.ok(analysisService.getAnalysisById(id));
    }

    @PostMapping("/patient/{patientId}")
    @Operation(
            summary = "رفع تحليل جديد",
            description = "يمكن رفع ملفات متعددة مع بيانات التحليل",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم رفع التحليل بنجاح"),
                    @ApiResponse(responseCode = "404",
                            description = "المريض أو التخصص غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class))),
                    @ApiResponse(responseCode = "400",
                            description = "الدكتور موجود بنفس الاسم والتخصص أو الملفات فارغة",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<String> uploadAnalysis(
            @PathVariable @Parameter(description = "معرف المريض", example = "ABCD1234") String patientId,
            @RequestParam("files") @Parameter(description = "ملفات التحليل") MultipartFile[] files,
            @Valid @ModelAttribute CreateAnalysisDto dto) {
        analysisService.uploadAnalysis(patientId, files, dto);
        return ResponseEntity.ok("تم رفع التحليل بنجاح");
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "تحديث تحليل موجود",
            description = "تحديث بيانات التحليل ورفع ملفات جديدة أو حذف ملفات موجودة",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم تحديث التحليل بنجاح"),
                    @ApiResponse(responseCode = "404",
                            description = "المريض أو التحليل أو التخصص غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class))),
                    @ApiResponse(responseCode = "400",
                            description = "الدكتور موجود بنفس الاسم والتخصص أو الملفات غير صحيحة",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<String> updateAnalysis(
            @PathVariable @Parameter(description = "معرف التحليل", example = "1") Long id,
            @RequestParam(value = "files", required = false) @Parameter(description = "ملفات جديدة") MultipartFile[] files,
            @ModelAttribute @Valid CreateAnalysisDto dto,
            @RequestParam(value = "imagesToDelete", required = false) @Parameter(description = "ملفات للحذف") List<String> imagesToDelete) {
        analysisService.updateAnalysis(id, files, dto, imagesToDelete);
        return ResponseEntity.ok("تم تحديث التحليل بنجاح");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "حذف تحليل",
            description = "يحذف التحليل المحدد",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم حذف التحليل بنجاح"),
                    @ApiResponse(responseCode = "404",
                            description = "التحليل غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<String> deleteAnalysis(
            @PathVariable @Parameter(description = "معرف التحليل", example = "1") Long id) {
        analysisService.deleteAnalysis(id);
        return ResponseEntity.ok("تم حذف التحليل بنجاح");
    }
}
