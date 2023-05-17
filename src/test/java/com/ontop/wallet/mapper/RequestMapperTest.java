package com.ontop.wallet.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ontop.wallet.dto.request.AccountRequestDTO;
import com.ontop.wallet.dto.request.DestinationDTO;
import com.ontop.wallet.dto.request.PaymentCreationRequestDTO;
import com.ontop.wallet.dto.request.SourceDTO;
import com.ontop.wallet.dto.request.TopUpTransactionRequest;
import com.ontop.wallet.dto.request.WithdrawTransactionRequestDTO;
import com.ontop.wallet.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {RequestMapper.class})
@ExtendWith(SpringExtension.class)
class RequestMapperTest {
    @Autowired
    private RequestMapper requestMapper;

    @Test
    void testGetTransaction() {
        BigDecimal amount = BigDecimal.valueOf(42L);
        Transaction actualTransaction = requestMapper.getTransaction(1L, amount, "Status",
                LocalDate.of(1970, 1, 1).atStartOfDay(), "Transaction Type");
        BigDecimal amount2 = actualTransaction.getAmount();
        assertSame(amount, amount2);
        assertEquals(1L, actualTransaction.getUserId().longValue());
        assertEquals("Status", actualTransaction.getStatus());
        assertEquals("Transaction Type", actualTransaction.getTransactionType());
        assertEquals("1970-01-01", actualTransaction.getCreatedAt().toLocalDate().toString());
        assertNull(actualTransaction.getId());
        assertEquals("42", amount2.toString());
    }


    @Test
    void testGetTopUpRequest() {
        TopUpTransactionRequest request = mock(TopUpTransactionRequest.class);
        when(request.getUserId()).thenReturn(1L);
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        when(request.getAmount()).thenReturn(valueOfResult);
        WithdrawTransactionRequestDTO actualTopUpRequest = requestMapper.getTopUpRequest(request);
        assertNull(actualTopUpRequest.getAccountNumber());
        assertEquals(1L, actualTopUpRequest.getUserId().longValue());
        assertNull(actualTopUpRequest.getSurname());
        assertNull(actualTopUpRequest.getRoutingNumber());
        assertNull(actualTopUpRequest.getNationalIdentificationNumber());
        assertNull(actualTopUpRequest.getName());
        BigDecimal amount = actualTopUpRequest.getAmount();
        assertSame(valueOfResult, amount);
        assertEquals("42", amount.toString());
        verify(request).getUserId();
        verify(request).getAmount();
    }


    @Test
    void testGetPaymentCreationRequest() {
        WithdrawTransactionRequestDTO requestDTO = mock(WithdrawTransactionRequestDTO.class);
        when(requestDTO.getAccountNumber()).thenReturn("42");
        when(requestDTO.getName()).thenReturn("Name");
        when(requestDTO.getRoutingNumber()).thenReturn("42");
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        when(requestDTO.getAmount()).thenReturn(valueOfResult);
        PaymentCreationRequestDTO actualPaymentCreationRequest = requestMapper.getPaymentCreationRequest(requestDTO);
        BigDecimal amount = actualPaymentCreationRequest.getAmount();
        assertSame(valueOfResult, amount);
        SourceDTO source = actualPaymentCreationRequest.getSource();
        assertEquals("COMPANY", source.getType());
        DestinationDTO destination = actualPaymentCreationRequest.getDestination();
        assertEquals("Name", destination.getName());
        assertEquals("42", amount.toString());
        AccountRequestDTO account = source.getAccount();
        assertEquals("${ontop.account.routing.number}", account.getRoutingNumber());
        assertEquals("${ontop.account.currency}", account.getCurrency());
        assertEquals("${ontop.account.number}", account.getAccountNumber());
        assertEquals("ONTOP INC", source.getSourceInformation().getName());
        AccountRequestDTO account2 = destination.getAccount();
        assertEquals("42", account2.getRoutingNumber());
        assertEquals("42", account2.getAccountNumber());
        assertEquals("USD", account2.getCurrency());
        verify(requestDTO).getAccountNumber();
        verify(requestDTO).getName();
        verify(requestDTO).getRoutingNumber();
        verify(requestDTO).getAmount();
    }
}

