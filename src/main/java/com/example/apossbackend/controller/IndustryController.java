package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.DetailCategoryDTO;
import com.example.apossbackend.service.IndustryService;
import com.example.apossbackend.service.impl.IndustryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/industry")
public class IndustryController {

    private final IndustryService industryService;

    public  IndustryController (IndustryService industryService){
        this.industryService = industryService;
    }

    @GetMapping("/detail_category")
    public List<DetailCategoryDTO> getAllDetailCategory(){
        return industryService.getAllDetailCategory();
    }

    @PostMapping("/add_industry")
    @PreAuthorize("HasRole('Admin')")
    public ResponseEntity<String> addDetailCategory(@RequestBody DetailCategoryDTO detailCategoryDTO){
        industryService.addIndustry(detailCategoryDTO);
        return ResponseEntity.ok("add industry success");
    }

    @DeleteMapping("/delete_industry")
    @PreAuthorize("HasRole('Admin')")
    public ResponseEntity<String> deleteDetailCategory(@RequestBody long id){
        industryService.deleteIndustry(id);
        return ResponseEntity.ok("Delete industry success");
    }

    @PutMapping("/update_industry")
    @PreAuthorize("Hasrole('Admin')")
    public ResponseEntity<String> updateDetailCategory(@RequestBody DetailCategoryDTO newDetailCategoryDTO,
                                                       @RequestBody long id){
        industryService.updateIndustry(newDetailCategoryDTO, id);
        return ResponseEntity.ok("Update industry success");
    }
}
