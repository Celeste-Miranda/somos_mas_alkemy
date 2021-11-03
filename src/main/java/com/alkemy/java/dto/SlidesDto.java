package com.alkemy.java.dto;

import com.alkemy.java.model.Slide;
import lombok.Data;

@Data
public class SlidesDto {

    private String text;
    private String imageUrl;
    private int numberOrder;

    public SlidesDto(Slide slide){
        this.text = slide.getText();
        this.imageUrl = slide.getImageUrl();
        this.numberOrder = slide.getNumberOrder();
    }

}
