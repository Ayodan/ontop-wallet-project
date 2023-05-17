package com.ontop.wallet.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontop.wallet.dto.response.PaymentCreationResponseDTO;
import com.ontop.wallet.dto.response.WalletBalanceResponseDTO;
import com.ontop.wallet.dto.response.WalletTransactionResponseDTO;
import com.ontop.wallet.exception.InternalServerException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectMapperUtil {

    public static String objectToJson(Object object) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerException("GENERIC_ERROR", "something bad happened");
        }
    }

    public static PaymentCreationResponseDTO jsonToPaymentCreationResponseDTO(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, PaymentCreationResponseDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerException("GENERIC_ERROR", "something bad happened");
        }
    }

    public static WalletTransactionResponseDTO jsonToWalletTransactionResponseDTO(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, WalletTransactionResponseDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerException("GENERIC_ERROR", "something bad happened");
        }
    }

    public static WalletBalanceResponseDTO jsonToWalletBalanceResponseDTO(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, WalletBalanceResponseDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerException("GENERIC_ERROR", "something bad happened");
        }
    }
}
