package com.ontop.wallet.util;

import com.ontop.wallet.Constant;
import com.ontop.wallet.transaction_enum.TransactionStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionStatusMapperUtil {
    public static TransactionStatus mapTransactionStatus(String status) {
        if (Constant.PROCESSING.equals(status) || "Success".equals(status))
            return TransactionStatus.SUCCESS;
        else
            return TransactionStatus.FAILED;
    }
}
