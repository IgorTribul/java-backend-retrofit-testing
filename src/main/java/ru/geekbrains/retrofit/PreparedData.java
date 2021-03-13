package ru.geekbrains.retrofit;

import com.github.javafaker.Faker;
import ru.geekbrains.mybatis.model.Products;
import ru.geekbrains.retrofit.dto.Product;

public class PreparedData {
    private Faker faker = new Faker();

    public Product getNewProduct(){
        return  new Product()
                .withTitle(faker.food().ingredient())
                .withPrice((int) (Math.random()*1000+1))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
    }

    public Products getNewProducts(){
        return  new Products(faker.food().ingredient(),
                (int) (Math.random()*1000+1), Long.valueOf(CategoryType.FOOD.getId()));
    }
}
