package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.DetailCategoryDTO;
import com.example.apossbackend.model.dto.KindDTO;
import com.example.apossbackend.model.entity.KindEntity;
import com.example.apossbackend.service.KindService;
import com.example.apossbackend.utils.AppConstants;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/kind")
public class KindController {

    private final KindService kindService;

    public KindController(KindService kindService) {
        this.kindService = kindService;
    }

    @GetMapping()
    public ResponseEntity<List<KindDTO>> getAllKind(
            @RequestParam(value = "categoryId", defaultValue = AppConstants.DEFAULT_CATEGORY_ID, required = false) long categoryId
    ) {
        if (categoryId != -1) {
            return ResponseEntity.ok(kindService.getAllKindByCategoryId(categoryId));
        } else {
            return ResponseEntity.ok(kindService.getAllKind());
        }
    }

    @PostMapping("/add_kind")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addKind(@RequestBody KindDTO kindDTO,
                                          @RequestBody long industryId){
        kindService.addKind(kindDTO, industryId);
        return ResponseEntity.ok("add industry success");
    }

    @DeleteMapping("/delete_kind")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteKind(@RequestBody long id){
        kindService.deleteKind(id);
        return ResponseEntity.ok("Delete industry success");
    }

    @PutMapping("/update_kind")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateKind(@RequestBody KindDTO kindDTO,
                                                       @RequestBody long id) {
        kindService.updateKind(kindDTO, id);
        return ResponseEntity.ok("Update industry success");
    }
}
