package com.ontop.wallet.mapper;

import com.ontop.wallet.Constant;
import com.ontop.wallet.dto.request.*;
import com.ontop.wallet.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class RequestMapper {

    @Value("${ontop.account.number}")
    private String accountNumber;
    @Value("${ontop.account.currency}")
    private String currency;
    @Value("${ontop.account.routing.number}")
    private String routingNumber;

    public Transaction getTransaction (Long userId, BigDecimal amount, String status, LocalDateTime now, String transactionType){
        return Transaction.builder()
                .userId(userId)
                .amount(amount.abs())
                .status(status)
                .TransactionType(transactionType)
                .createdAt(now)
                .build();
    }

    public WithdrawTransactionRequestDTO getTopUpRequest (TopUpTransactionRequest request){
        return WithdrawTransactionRequestDTO.builder()
                .userId(request.getUserId())
                .amount(request.getAmount())
                .build();
    }

    public PaymentCreationRequestDTO getPaymentCreationRequest (WithdrawTransactionRequestDTO requestDTO){
       return  PaymentCreationRequestDTO.builder()
                .source(SourceDTO.builder()
                        .account(AccountRequestDTO.builder()
                                .accountNumber(accountNumber)
                                .currency(currency)
                                .routingNumber(routingNumber)
                                .build())
                        .sourceInformation(SourceInformationDTO.builder()
                                .name(Constant.SOURCE_INFORMATION_NAME)
                                .build())
                        .type(Constant.SOURCE_TYPE)
                        .build())
                .destination(DestinationDTO.builder()
                        .account(AccountRequestDTO.builder()
                                .accountNumber(requestDTO.getAccountNumber())
                                .routingNumber(requestDTO.getRoutingNumber())
                                .currency(Constant.DESTINATION_CURRENCY)
                                .build())
                        .name(requestDTO.getName())
                        .build())
               .amount(requestDTO.getAmount().abs())
                .build();
    }
}
