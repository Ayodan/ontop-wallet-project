package com.ontop.wallet.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ontop.wallet.dto.response.WalletBalanceResponseDTO;
import com.ontop.wallet.dto.response.WalletTransactionResponseDTO;
import com.ontop.wallet.model.Wallet;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ResponseMapper.class})
@ExtendWith(SpringExtension.class)
class ResponseMapperTest {
    @Autowired
    private ResponseMapper responseMapper;

    @Test
    void testMapWalletTransactionResponse() {
        BigDecimal amount = BigDecimal.valueOf(42L);
        WalletTransactionResponseDTO actualMapWalletTransactionResponseResult = responseMapper
                .mapWalletTransactionResponse(1L, amount, 1L);
        BigDecimal amount2 = actualMapWalletTransactionResponseResult.getAmount();
        assertSame(amount, amount2);
        assertEquals(1L, actualMapWalletTransactionResponseResult.getWalletTransactionId().longValue());
        assertEquals(1L, actualMapWalletTransactionResponseResult.getUserId().longValue());
        assertEquals("42", amount2.toString());
    }


    @Test
    void testMapWalletBalanceResponse() {
        Wallet wallet = mock(Wallet.class);
        when(wallet.getUserId()).thenReturn(1L);
        when(wallet.getBalance()).thenReturn(BigDecimal.valueOf(42L));
        doNothing().when(wallet).setBalance(Mockito.<BigDecimal>any());
        doNothing().when(wallet).setId(Mockito.<Long>any());
        doNothing().when(wallet).setUserId(Mockito.<Long>any());
        BigDecimal balance = BigDecimal.valueOf(42L);
        wallet.setBalance(balance);
        wallet.setId(1L);
        wallet.setUserId(1L);
        WalletBalanceResponseDTO actualMapWalletBalanceResponseResult = responseMapper.mapWalletBalanceResponse(wallet);
        BigDecimal balance2 = actualMapWalletBalanceResponseResult.getBalance();
        assertEquals(balance, balance2);
        assertEquals(1L, actualMapWalletBalanceResponseResult.getUserId().longValue());
        assertEquals("42", balance2.toString());
        verify(wallet).getUserId();
        verify(wallet).getBalance();
        verify(wallet).setBalance(Mockito.<BigDecimal>any());
        verify(wallet).setId(Mockito.<Long>any());
        verify(wallet).setUserId(Mockito.<Long>any());
    }
}

