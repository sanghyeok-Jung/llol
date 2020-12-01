package com.project.llol.dto;

import lombok.Data;

@Data
public class ItemGoldDTO {
    private int base;
    private boolean purchasable;
    private int total;
    private int sell;
}
