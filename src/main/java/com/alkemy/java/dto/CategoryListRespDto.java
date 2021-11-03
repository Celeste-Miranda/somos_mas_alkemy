package com.alkemy.java.dto;

import com.alkemy.java.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListRespDto {

    private String name;

    public CategoryListRespDto(Category category) {
        this.name = category.getName();
    }
}
