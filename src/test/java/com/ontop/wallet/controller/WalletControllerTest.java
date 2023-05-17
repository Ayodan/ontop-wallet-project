package com.ontop.wallet.controller;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontop.wallet.dto.request.TopUpTransactionRequest;
import com.ontop.wallet.dto.request.WithdrawTransactionRequestDTO;
import com.ontop.wallet.dto.response.WalletBalanceResponseDTO;
import com.ontop.wallet.dto.response.WalletTransactionResponseDTO;
import com.ontop.wallet.service.WalletService;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {WalletController.class})
@ExtendWith(SpringExtension.class)
class WalletControllerTest {
    @Autowired
    private WalletController walletController;

    @MockBean
    private WalletService walletService;

    @Test
    void testGetBalance() throws Exception {
        WalletBalanceResponseDTO walletBalanceResponseDTO = mock(WalletBalanceResponseDTO.class);
        when(walletBalanceResponseDTO.getUserId()).thenReturn(1L);
        when(walletBalanceResponseDTO.getBalance()).thenReturn(BigDecimal.valueOf(42L));
        when(walletService.getWalletBalance(Mockito.<Long>any())).thenReturn(walletBalanceResponseDTO);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/wallets/balance");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("user_id", String.valueOf(1L));
        MockMvcBuilders.standaloneSetup(walletController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"balance\":42,\"user_id\":1}"));
    }

    @Test
    void testGetWalletTransactions() throws Exception {
        when(walletService.getUserWalletTransactions(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any(),
                anyInt(), anyInt())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/wallets/transactions");
        MockHttpServletRequestBuilder paramResult = getResult.param("page", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(walletController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testWithdraw() throws Exception {
        WalletTransactionResponseDTO walletTransactionResponseDTO = mock(WalletTransactionResponseDTO.class);
        when(walletTransactionResponseDTO.getUserId()).thenReturn(1L);
        when(walletTransactionResponseDTO.getWalletTransactionId()).thenReturn(1L);
        when(walletTransactionResponseDTO.getAmount()).thenReturn(BigDecimal.valueOf(42L));
        when(walletService.performWithdrawFromWallet(Mockito.<WithdrawTransactionRequestDTO>any()))
                .thenReturn(walletTransactionResponseDTO);

        WithdrawTransactionRequestDTO withdrawTransactionRequestDTO = new WithdrawTransactionRequestDTO();
        withdrawTransactionRequestDTO.setAccountNumber("42");
        withdrawTransactionRequestDTO.setAmount(BigDecimal.valueOf(42L));
        withdrawTransactionRequestDTO.setName("Name");
        withdrawTransactionRequestDTO.setNationalIdentificationNumber("42");
        withdrawTransactionRequestDTO.setRoutingNumber("42");
        withdrawTransactionRequestDTO.setSurname("Doe");
        withdrawTransactionRequestDTO.setUserId(1L);
        String content = (new ObjectMapper()).writeValueAsString(withdrawTransactionRequestDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/wallets/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(walletController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"amount\":42,\"wallet_transaction_id\":1,\"user_id\":1}"));
    }
}

