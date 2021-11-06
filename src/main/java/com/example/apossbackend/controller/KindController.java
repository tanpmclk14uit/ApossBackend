package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.KindDTO;
import com.example.apossbackend.model.entity.KindEntity;
import com.example.apossbackend.service.KindService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("kind")
public class KindController {

    private final KindService kindService;

    public KindController(KindService kindService)
    {
        this.kindService = kindService;
    }

    @GetMapping("/kind_dto")
    public List<KindDTO> getAllKind()
    {
        return kindService.getAllKind();
    }
}
