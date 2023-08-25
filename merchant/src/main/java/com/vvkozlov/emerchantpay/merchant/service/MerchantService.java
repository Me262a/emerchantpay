package com.vvkozlov.emerchantpay.merchant.service;

import com.vvkozlov.emerchantpay.merchant.domain.constants.MerchantStatusEnum;
import com.vvkozlov.emerchantpay.merchant.domain.entities.Merchant;
import com.vvkozlov.emerchantpay.merchant.infra.repository.MerchantRepository;
import com.vvkozlov.emerchantpay.merchant.service.mapper.MerchantMapper;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantEditDTO;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantViewDTO;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;
import com.vvkozlov.emerchantpay.merchant.service.util.ProfilesEnum;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The Merchant service to get, list, import, update, remove merchants.
 */
@Service
public class MerchantService {
    @Value("${spring.profiles.active:default}")
    private String activeProfile;
    private final MerchantRepository merchantRepository;

    /**
     * Instantiates a new Merchant service.
     *
     * @param merchantRepository the merchant repository
     */
    @Autowired
    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    /**
     * Gets merchant.
     *
     * @param id id of merchant to retrieve
     * @return Operation result. If successful, it will contain the merchant dto.
     */
    @Transactional(readOnly = true)
    public OperationResult<MerchantViewDTO> getMerchant(final String id) {
        Optional<Merchant> merchantOpt = merchantRepository.findById(UUID.fromString(id));
        if (merchantOpt.isPresent()) {
            Merchant merchant = merchantOpt.get();
            MerchantViewDTO viewDTO = MerchantMapper.INSTANCE.toDto(merchant);
            return OperationResult.success(viewDTO);
        } else {
            return OperationResult.failure("Merchant not found");
        }
    }

    /**
     * Gets pageable list of merchants.
     *
     * @param pageable the pageable
     * @return Operation result. If successful, it will contain the merchants page.
     */
    @Transactional(readOnly = true)
    public OperationResult<Page<MerchantViewDTO>> getMerchants(final Pageable pageable) {
        Page<Merchant> merchants = merchantRepository.findAll(pageable);
        List<MerchantViewDTO> viewDTOs = merchants.stream()
                .map(MerchantMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
        return OperationResult.success(new PageImpl<>(viewDTOs, pageable, merchants.getTotalElements()));
    }

    /**
     * Update a merchant
     *
     * @param id  the id of record being updated
     * @param dto the merchant edit dto
     * @return the operation result containing updated Merchant DTO
     */
    @Transactional
    public OperationResult<MerchantViewDTO> updateMerchant(final String id, final MerchantEditDTO dto) {
        try {
            Optional<Merchant> existingMerchant = merchantRepository.findById(UUID.fromString(id));

            if (existingMerchant.isPresent()) {
                Merchant merchantToUpdate = existingMerchant.get();
                Merchant updatedMerchant = MerchantMapper.INSTANCE.toEntity(dto);

                merchantToUpdate.setName(updatedMerchant.getName());
                merchantToUpdate.setDescription(updatedMerchant.getDescription());
                merchantToUpdate.setEmail(updatedMerchant.getEmail());
                merchantToUpdate.setStatus(updatedMerchant.getStatus());

                Merchant savedMerchant = merchantRepository.save(merchantToUpdate);

                MerchantViewDTO viewDTO = MerchantMapper.INSTANCE.toDto(savedMerchant);
                return OperationResult.success(viewDTO);
            } else {
                return OperationResult.failure("Merchant not found");
            }
        } catch (Exception e) {
            if (!ProfilesEnum.PROD.getProfileName().equals(activeProfile)) {
                return OperationResult.failure("An error occurred while updating the merchant.");
            } else {
                return OperationResult.failure("An error occurred while updating the merchant: " + e.getMessage());
            }
        }
    }

    /**
     * Import merchants from csv operation result.
     *
     * @return the operation result
     */
    @Transactional
    public OperationResult<List<MerchantViewDTO>> importMerchantsFromCsv() {
        try {
            ClassPathResource resource = new ClassPathResource("csv/merchants.csv");
            Reader reader = new InputStreamReader(resource.getInputStream());

            var format = CSVFormat.Builder.create()
                    .setHeader("name", "description", "email", "status")
                    .setSkipHeaderRecord(true)
                    .setDelimiter(',')
                    .build();

            CSVParser csvParser = new CSVParser(reader, format);
            List<MerchantViewDTO> merchantsImported = new ArrayList<>();

            for (CSVRecord record : csvParser) {
                Merchant merchant = new Merchant();
                merchant.setName(record.get("name"));
                merchant.setDescription(record.get("description"));
                merchant.setEmail(record.get("email"));
                merchant.setStatus(MerchantStatusEnum.valueOf(record.get("status")));
                merchant.setTotalTransactionSum(0.0);

                merchant = merchantRepository.save(merchant);
                merchantsImported.add(MerchantMapper.INSTANCE.toDto(merchant));
            }

            reader.close();
            csvParser.close();
            return OperationResult.success(merchantsImported);
        } catch (Exception e) {
            return OperationResult.failure("An error occurred while importing merchants: " + e.getMessage());
        }
    }
}