package com.mercury.platform.shared.entity.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CurrencyMessage extends Message {
    private Double currForSaleCount;
    private String currForSaleTitle;
}
