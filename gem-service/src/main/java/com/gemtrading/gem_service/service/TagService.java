package com.gemtrading.gem_service.service;

import com.gemtrading.gem_service.exception.DuplicateResourceException;
import com.gemtrading.gem_service.exception.ResourceNotFoundException;
import com.gemtrading.gem_service.dto.TagRequest;
import com.gemtrading.gem_service.dto.TagResponse;
import com.gemtrading.gem_service.entity.GemStone;
import com.gemtrading.gem_service.entity.Tag;
import com.gemtrading.gem_service.repository.GemStoneRepository;
import com.gemtrading.gem_service.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final GemStoneRepository gemStoneRepository;

    public TagResponse createTag(TagRequest request) {
        if (tagRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Tag with name '" + request.getName() + "' already exists");
        }
        Tag tag = Tag.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return toResponse(tagRepository.save(tag));
    }

    public TagResponse getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", String.valueOf(id)));
        return toResponse(tag);
    }

    public Tag getTagEntityById(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", String.valueOf(tagId)));
    }

    public Page<TagResponse> getAllTags(Pageable pageable) {
        return tagRepository.findAll(pageable).map(this::toResponse);
    }

    public TagResponse updateTag(Long id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", String.valueOf(id)));

        if (!tag.getName().equalsIgnoreCase(request.getName())
                && tagRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Tag with name '" + request.getName() + "' already exists");
        }

        tag.setName(request.getName());
        tag.setDescription(request.getDescription());
        return toResponse(tagRepository.save(tag));
    }

    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", String.valueOf(id)));
        tag.getGemStones().forEach(gem -> gem.getTags().remove(tag));
        tagRepository.delete(tag);
    }

    public TagResponse addTagToGemStone(Long tagId, Long gemId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", String.valueOf(tagId)));
        GemStone gemStone = gemStoneRepository.findById(gemId)
                .orElseThrow(() -> new ResourceNotFoundException("GemStone", String.valueOf(gemId)));
        gemStone.getTags().add(tag);
        gemStoneRepository.save(gemStone);
        return toResponse(tagRepository.findById(tagId).get());
    }

    public TagResponse removeTagFromGemStone(Long tagId, Long gemId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", String.valueOf(tagId)));
        GemStone gemStone = gemStoneRepository.findById(gemId)
                .orElseThrow(() -> new ResourceNotFoundException("GemStone", String.valueOf(gemId)));
        gemStone.getTags().remove(tag);
        gemStoneRepository.save(gemStone);
        return toResponse(tagRepository.findById(tagId).get());
    }

    private TagResponse toResponse(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .description(tag.getDescription())
                .gemStoneCount(tag.getGemStones().size())
                .createdAt(tag.getCreatedAt())
                .build();
    }
}
