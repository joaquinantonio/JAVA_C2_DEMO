package com.example.assettracker.service;

import com.example.assettracker.dto.AssetResponse;
import com.example.assettracker.dto.CreateAssetRequest;
import com.example.assettracker.exception.ResourceNotFoundException;
import com.example.assettracker.model.Asset;
import com.example.assettracker.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * AssetService
 * ------------
 * Services contain business logic. They are simple POJOs annotated with
 * @Service so Spring will detect and manage them (as beans) during startup.
 *
 * This example uses an in-memory list to keep the example simple for students.
 * In production you would typically talk to a database via a Repository.
 */
@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<AssetResponse> getAllAssets() {
        return assetRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AssetResponse getAssetById(String id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset " + id + " was not found"));

        return toResponse(asset);
    }

    public AssetResponse createAsset(CreateAssetRequest request) {
        Asset asset = new Asset(
                request.getAssetTag().trim(),
                request.getName().trim(),
                request.getCategory().trim(),
                request.getSerialNumber().trim(),
                "AVAILABLE",
                request.getLocation().trim(),
                null
        );

        Asset savedAsset = assetRepository.save(asset);
        return toResponse(savedAsset);
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
