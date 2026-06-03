package com.gemtrading.gem_service.service;

import com.gemtrading.gem_service.dto.GemStoneRequest;
import com.gemtrading.gem_service.dto.GemStoneResponse;
import com.gemtrading.gem_service.entity.GemStone;
import com.gemtrading.gem_service.repository.GemStoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GemStoneService {


    private final GemStoneRepository gemStoneRepository;


    public Page<GemStoneResponse> getAllGemStone(Pageable pageable){
        return gemStoneRepository.findByActiveTrue(pageable).map(this::toResponse);
    }
    public GemStoneResponse createGemStone (GemStoneRequest request){

        GemStone gemStone = GemStone.builder()
                .gemCode(request.getGemCode())
                .type(request.getType())
                .origin(request.getOrigin())
                .caratWeight(request.getCaratWeight())
                .color(request.getColor())
                .description(request.getDescription())
                .stockQuantity(request.getStockQuantity())
                .pricePerCarat(request.getPricePerCarat())
                .description(request.getDescription())
                .treatment(request.getTreatment())
                .active(true)
                .build();

        return toResponse(gemStoneRepository.save(gemStone));
    }

    private GemStoneResponse toResponse(GemStone gemStone){
        return GemStoneResponse.builder()
                .id(gemStone.getId())
                .gemCode(gemStone.getGemCode())
                .type(gemStone.getType())
                .color(gemStone.getColor())
                .caratWeight(gemStone.getCaratWeight())
                .origin(gemStone.getOrigin())
                .treatment(gemStone.getTreatment())
                .pricePerCarat(gemStone.getPricePerCarat())
                .stockQuantity(gemStone.getStockQuantity())
                .certified(gemStone.isCertified())
                .createdAt(gemStone.getCreatedAt())
                .updatedAt(gemStone.getUpdatedAt())
                .description(gemStone.getDescription())
                .build();
    }

    public GemStoneResponse getGemStoneById(Long id) {
        return toResponse(gemStoneRepository.findById(id).get());
    }
}
