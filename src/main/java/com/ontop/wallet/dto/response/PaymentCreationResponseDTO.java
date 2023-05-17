package com.ontop.wallet.dto.response;

import lombok.Data;

@Data
public class PaymentCreationResponseDTO {
    private RequestInfoDTO requestInfo;
    private PaymentInfoDTO paymentInfo;
}
