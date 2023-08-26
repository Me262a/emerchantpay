package com.vvkozlov.emerchantpay.merchant.service;


import com.vvkozlov.emerchantpay.merchant.domain.constants.UserRoles;
import com.vvkozlov.emerchantpay.merchant.service.contract.OAuthServerAdminClient;
import com.vvkozlov.emerchantpay.merchant.service.model.AdminViewDTO;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * The Admin service to import admins.
 */
@Service
public class AdminService {
    private final OAuthServerAdminClient oAuthServerAdminClient;

    @Autowired
    public AdminService(OAuthServerAdminClient oAuthServerAdminClient) {
        this.oAuthServerAdminClient = oAuthServerAdminClient;
    }

    /**
     * Import merchants from fake csv.
     *
     * @return the operation result
     */
    public OperationResult<List<AdminViewDTO>> importAdminsFromCsv() {
        try {
            ClassPathResource resource = new ClassPathResource("csv/admins.csv");
            Reader reader = new InputStreamReader(resource.getInputStream());

            var format = CSVFormat.Builder.create()
                    .setHeader("name", "description", "email", "status")
                    .setSkipHeaderRecord(true)
                    .setDelimiter(',')
                    .build();

            CSVParser csvParser = new CSVParser(reader, format);
            List<AdminViewDTO> adminsImported = new ArrayList<>();
            List<String> adminRoles = List.of(UserRoles.ROLE_ADMIN);

            for (CSVRecord record : csvParser) {
                AdminViewDTO admin = new AdminViewDTO();
                admin.setName(record.get("name"));
                admin.setDescription(record.get("description"));
                admin.setEmail(record.get("email"));

                //TODO: Adding one user at once is slow, consider some batch process
                var oAuthCreateUserResult =  oAuthServerAdminClient
                        .addUser(admin.getEmail(), adminRoles);
                if (oAuthCreateUserResult.isSuccess()) {
                    admin.setId(oAuthCreateUserResult.getResult());
                    adminsImported.add(admin);
                }
            }

            reader.close();
            csvParser.close();
            return OperationResult.success(adminsImported);
        } catch (Exception e) {
            //TODO: If failing user was created on auth server, rollback it there
            return OperationResult.failure("An error occurred while importing admins: " + e.getMessage());
        }
    }
}