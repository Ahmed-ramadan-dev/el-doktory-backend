package com.spring.boot.Controller;

import com.spring.boot.Dto.CreateScanDto;
import com.spring.boot.Dto.GetScanDto;
import com.spring.boot.Dto.ScanDto;
import com.spring.boot.Dto.Vm.ExceptionResponseVm;
import com.spring.boot.Service.ScanService;
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
@RequestMapping("api/scan/")
public class ScanController {

    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @GetMapping("/patient/{patientId}")
    @Operation(
            summary = "جلب كل الأشعات لمريض",
            description = "يعرض كل الأشعات الخاصة بالمريض",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم جلب الأشعات بنجاح",
                            content = @Content(schema = @Schema(implementation = ScanDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "المريض غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<List<ScanDto>> getAllScans(
            @PathVariable @Parameter(description = "معرف المريض", example = "ABCD1234") String patientId) {
        return ResponseEntity.ok(scanService.getAllScans(patientId));
    }

    @GetMapping("/patient/{patientId}/search")
    @Operation(
            summary = "البحث في الأشعات",
            description = "يمكن البحث بالاسم أو بالتاريخ",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم البحث بنجاح",
                            content = @Content(schema = @Schema(implementation = ScanDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "المريض غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<List<ScanDto>> searchScans(
            @PathVariable @Parameter(description = "معرف المريض", example = "ABCD1234") String patientId,
            @RequestParam(required = false) @Parameter(description = "كلمة البحث في الاسم", example = "Chest X-ray") String keyword,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "تاريخ البحث", example = "2025-12-21") LocalDate date) {
        return ResponseEntity.ok(scanService.searchScans(patientId, keyword, date));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "جلب أشعة واحدة",
            description = "يعرض تفاصيل أشعة محددة",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم جلب الأشعة بنجاح",
                            content = @Content(schema = @Schema(implementation = GetScanDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "الأشعة غير موجودة",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<GetScanDto> getScanById(
            @PathVariable @Parameter(description = "معرف الأشعة", example = "1") Long id) {
        return ResponseEntity.ok(scanService.getScanById(id));
    }

    @PostMapping("/patient/{patientId}")
    @Operation(
            summary = "رفع أشعة جديدة",
            description = "يمكن رفع ملفات متعددة مع بيانات الأشعة",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم رفع الأشعة بنجاح"),
                    @ApiResponse(responseCode = "404",
                            description = "المريض أو التخصص غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class))),
                    @ApiResponse(responseCode = "400",
                            description = "الدكتور موجود بنفس الاسم والتخصص أو الملفات فارغة",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<String> uploadScan(
            @PathVariable @Parameter(description = "معرف المريض", example = "ABCD1234") String patientId,
            @RequestParam("files") @Parameter(description = "ملفات الأشعة") MultipartFile[] files,
            @Valid @ModelAttribute CreateScanDto dto) {
        scanService.uploadScan(patientId, files, dto);
        return ResponseEntity.ok("تم رفع الأشعة بنجاح");
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "تحديث أشعة موجودة",
            description = "تحديث بيانات الأشعة ورفع ملفات جديدة أو حذف ملفات موجودة",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم تحديث الأشعة بنجاح"),
                    @ApiResponse(responseCode = "404",
                            description = "المريض أو الأشعة أو التخصص غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class))),
                    @ApiResponse(responseCode = "400",
                            description = "الدكتور موجود بنفس الاسم والتخصص أو الملفات غير صحيحة",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<String> updateScan(
            @PathVariable @Parameter(description = "معرف الأشعة", example = "1") Long id,
            @RequestParam(value = "files", required = false) @Parameter(description = "ملفات جديدة") MultipartFile[] files,
            @ModelAttribute @Valid CreateScanDto dto,
            @RequestParam(value = "imagesToDelete", required = false) @Parameter(description = "ملفات للحذف") List<String> imagesToDelete) {
        scanService.updateScan(id, files, dto, imagesToDelete);
        return ResponseEntity.ok("تم تحديث الأشعة بنجاح");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "حذف أشعة",
            description = "يحذف الأشعة المحددة",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم حذف الأشعة بنجاح"),
                    @ApiResponse(responseCode = "404",
                            description = "الأشعة غير موجودة",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<String> deleteScan(
            @PathVariable @Parameter(description = "معرف الأشعة", example = "1") Long id) {
        scanService.deleteScan(id);
        return ResponseEntity.ok("تم حذف الأشعة بنجاح");
    }
}
