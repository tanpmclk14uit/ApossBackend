package com.example.apossbackend.service;

import com.example.apossbackend.model.dto.KindDTO;

import java.util.List;

public interface KindService {
    List<KindDTO> getAllKind();
    List<KindDTO> getAllKindByCategoryId(Long categoryId);

    void deleteKind(long id);

    void addKind(KindDTO kindDTO, long industryId);

    void updateKind(KindDTO kindDTO, long id);
}
