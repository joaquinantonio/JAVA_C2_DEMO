package com.example.assettracker.service;

import com.example.assettracker.dto.AssetResponse;
import com.example.assettracker.dto.CreateAssetRequest;
import com.example.assettracker.exception.ResourceNotFoundException;
import com.example.assettracker.exception.DuplicateResourceException;
import com.example.assettracker.model.Asset;
import com.example.assettracker.repository.AssetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * AssetService
 * ------------
 * Services contain business logic. This Day 8 version uses AssetRepository
 * to query MongoDB documents, add filtering, add pagination/sorting, and log
 * important service operations.
 */
@Service
public class AssetService {

    private static final Logger logger = LoggerFactory.getLogger(AssetService.class);

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<AssetResponse> getAssets(String status, String category, String location) {
        logger.info("Fetching assets with status={}, category={}, location={}", status, category, location);

        List<Asset> assets;

        if (hasValue(status)) {
            assets = assetRepository.findByStatusIgnoreCase(status.trim());
        } else if (hasValue(category)) {
            assets = assetRepository.findByCategoryIgnoreCase(category.trim());
        } else if (hasValue(location)) {
            assets = assetRepository.findByLocationContainingIgnoreCase(location.trim());
        } else {
            assets = assetRepository.findAll();
        }

        logger.info("Found {} asset(s)", assets.size());

        return assets.stream()
                .map(this::toResponse)
                .toList();
    }

    public Page<AssetResponse> getAssetsPaged(int page, int size, String sortBy, String direction) {
        logger.info("Fetching paged assets page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return assetRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public AssetResponse getAssetById(String id) {
        logger.info("Fetching asset by id={}", id);

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset " + id + " was not found"));

        return toResponse(asset);
    }

    public AssetResponse createAsset(CreateAssetRequest request) {
        String assetTag = request.getAssetTag().trim();
        String serialNumber = request.getSerialNumber().trim();

        if (assetRepository.existsByAssetTag(assetTag)) {
            throw new DuplicateResourceException("Asset tag already exists: " + assetTag);
        }

        if (assetRepository.existsBySerialNumber(serialNumber)) {
            throw new DuplicateResourceException("Serial number already exists: " + serialNumber);
        }

        Asset asset = new Asset(
                assetTag,
                request.getName().trim(),
                request.getCategory().trim(),
                serialNumber,
                "AVAILABLE",
                request.getLocation().trim(),
                null
        );

        Asset savedAsset = assetRepository.save(asset);
        return toResponse(savedAsset);
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }

    private AssetResponse toResponse(Asset asset) {
        return new AssetResponse(
                asset.getId(),
                asset.getAssetTag(),
                asset.getName(),
                asset.getCategory(),
                asset.getSerialNumber(),
                asset.getStatus(),
                asset.getLocation(),
                asset.getAssignedTo()
        );
    }
}
