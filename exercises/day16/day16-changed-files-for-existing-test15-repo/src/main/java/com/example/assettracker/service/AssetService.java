package com.example.assettracker.service;

import com.example.assettracker.dto.AssetResponse;
import com.example.assettracker.dto.CreateAssetRequest;
import com.example.assettracker.dto.UpdateAssetRequest;
import com.example.assettracker.exception.DuplicateResourceException;
import com.example.assettracker.exception.InvalidRequestException;
import com.example.assettracker.exception.ResourceNotFoundException;
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
import java.util.Set;

@Service
public class AssetService {

    private static final Logger logger = LoggerFactory.getLogger(AssetService.class);

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "assetTag",
            "name",
            "category",
            "serialNumber",
            "status",
            "location",
            "assignedTo"
    );

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "AVAILABLE",
            "ASSIGNED",
            "MAINTENANCE"
    );

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

        validatePageRequest(page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return assetRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public AssetResponse getAssetById(String id) {
        logger.info("Fetching asset by id={}", id);
        return toResponse(findAssetOrThrow(id));
    }

    public AssetResponse createAsset(CreateAssetRequest request) {
        String assetTag = normalizeRequired(request.getAssetTag());
        String serialNumber = normalizeRequired(request.getSerialNumber());

        ensureAssetTagIsUniqueForCreate(assetTag);
        ensureSerialNumberIsUniqueForCreate(serialNumber);

        Asset asset = new Asset(
                assetTag,
                normalizeRequired(request.getName()),
                normalizeRequired(request.getCategory()),
                serialNumber,
                "AVAILABLE",
                normalizeRequired(request.getLocation()),
                null
        );

        return toResponse(assetRepository.save(asset));
    }

    public AssetResponse updateAsset(String id, UpdateAssetRequest request) {
        logger.info("Updating asset id={}", id);

        Asset asset = findAssetOrThrow(id);
        String assetTag = normalizeRequired(request.getAssetTag());
        String serialNumber = normalizeRequired(request.getSerialNumber());
        String status = normalizeStatus(request.getStatus());

        ensureAssetTagIsUniqueForUpdate(asset, assetTag);
        ensureSerialNumberIsUniqueForUpdate(asset, serialNumber);

        asset.setAssetTag(assetTag);
        asset.setName(normalizeRequired(request.getName()));
        asset.setCategory(normalizeRequired(request.getCategory()));
        asset.setSerialNumber(serialNumber);
        asset.setStatus(status);
        asset.setLocation(normalizeRequired(request.getLocation()));
        asset.setAssignedTo(normalizeOptional(request.getAssignedTo()));

        return toResponse(assetRepository.save(asset));
    }

    private Asset findAssetOrThrow(String id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset " + id + " was not found"));
    }

    private void ensureAssetTagIsUniqueForCreate(String assetTag) {
        if (assetRepository.existsByAssetTag(assetTag)) {
            throw new DuplicateResourceException("Asset tag already exists: " + assetTag);
        }
    }

    private void ensureSerialNumberIsUniqueForCreate(String serialNumber) {
        if (assetRepository.existsBySerialNumber(serialNumber)) {
            throw new DuplicateResourceException("Serial number already exists: " + serialNumber);
        }
    }

    private void ensureAssetTagIsUniqueForUpdate(Asset asset, String assetTag) {
        if (!asset.getAssetTag().equalsIgnoreCase(assetTag) && assetRepository.existsByAssetTag(assetTag)) {
            throw new DuplicateResourceException("Asset tag already exists: " + assetTag);
        }
    }

    private void ensureSerialNumberIsUniqueForUpdate(Asset asset, String serialNumber) {
        if (!asset.getSerialNumber().equalsIgnoreCase(serialNumber) && assetRepository.existsBySerialNumber(serialNumber)) {
            throw new DuplicateResourceException("Serial number already exists: " + serialNumber);
        }
    }

    private void validatePageRequest(int page, int size, String sortBy, String direction) {
        if (page < 0) {
            throw new InvalidRequestException("Page must be zero or greater");
        }

        if (size < 1 || size > 50) {
            throw new InvalidRequestException("Size must be between 1 and 50");
        }

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new InvalidRequestException("Sort field is not allowed: " + sortBy);
        }

        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            throw new InvalidRequestException("Direction must be either asc or desc");
        }
    }

    private String normalizeStatus(String status) {
        String normalizedStatus = normalizeRequired(status).toUpperCase();

        if (!ALLOWED_STATUSES.contains(normalizedStatus)) {
            throw new InvalidRequestException("Status must be AVAILABLE, ASSIGNED or MAINTENANCE");
        }

        return normalizedStatus;
    }

    private String normalizeRequired(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
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
