package ru.geekbrains.retrofit.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@With
public class Product {

    @JsonProperty("id")
    @Getter
    private Integer id;
    @JsonProperty("title")
    @Getter
    private String title;
    @JsonProperty("price")
    @Getter
    private Integer price;
    @JsonProperty("categoryTitle")
    @Getter
    private String categoryTitle;

}