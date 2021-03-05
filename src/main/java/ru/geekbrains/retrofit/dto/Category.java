package ru.geekbrains.retrofit.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Category {

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("title")
    public String title;
    @JsonProperty("products")
    public List<Product> products = new ArrayList<Product>();

}