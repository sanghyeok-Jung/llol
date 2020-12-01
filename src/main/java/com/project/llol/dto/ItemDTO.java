package com.project.llol.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemDTO {
    private String key;
    private String name;
    private String plaintext;
    private String description;
    private ImageDTO image;
    private ItemGoldDTO gold;
    private List<String> from;
    private List<String> into;
}
