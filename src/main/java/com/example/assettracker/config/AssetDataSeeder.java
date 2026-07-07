package com.example.assettracker.config;

import com.example.assettracker.model.Asset;
import com.example.assettracker.repository.AssetRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssetDataSeeder {

    @Bean
    CommandLineRunner seedAssets(AssetRepository assetRepository) {
        return args -> {
            if (assetRepository.count() > 0) {
                return;
            }

            assetRepository.save(new Asset(
                    "LAP-2026-001",
                    "Dell Latitude 5440",
                    "Laptop",
                    "SN-LAP-001",
                    "AVAILABLE",
                    "HQ Level 3",
                    null
            ));

            assetRepository.save(new Asset(
                    "MON-2026-001",
                    "Dell 24-inch Monitor",
                    "Monitor",
                    "SN-MON-001",
                    "ASSIGNED",
                    "HQ Level 2",
                    "nurul@example.com"
            ));

            assetRepository.save(new Asset(
                    "PRJ-2026-001",
                    "Epson Projector",
                    "Projector",
                    "SN-PRJ-001",
                    "AVAILABLE",
                    "Training Room A",
                    null
            ));
        };
    }
}
