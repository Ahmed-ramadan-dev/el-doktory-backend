package com.spring.boot.Controller;

import com.spring.boot.Dto.DoctorDto;
import com.spring.boot.Dto.Vm.ExceptionResponseVm;
import com.spring.boot.Service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/patients/{patientId}/all")
    @Operation(
            summary = "جلب كل الدكاترة لمريض محدد",
            description = "يمكن البحث باسم الدكتور إذا تم تمرير query",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "تم جلب قائمة الدكاترة بنجاح",
                            content = @Content(schema = @Schema(implementation = DoctorDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "المريض غير موجود",
                            content = @Content(schema = @Schema(implementation = ExceptionResponseVm.class)))
            }
    )
    public ResponseEntity<List<DoctorDto>> getAllDoctorsForPatient(
            @PathVariable @Parameter(description = "معرف المريض", example = "ABCD1234") String patientId,
            @RequestParam(required = false)
            @Parameter(description = "كلمة البحث في اسم الدكتور", example = "Ahmed") String query) {
        return ResponseEntity.ok(doctorService.searchDoctors(patientId, query));
    }
}
