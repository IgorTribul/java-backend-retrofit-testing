package ru.geekbrains.retrofit;

import com.github.javafaker.Faker;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Converter;
import retrofit2.Response;
import ru.geekbrains.retrofit.CategoryType;
import ru.geekbrains.retrofit.dto.ErrorBody;
import ru.geekbrains.retrofit.dto.Product;
import ru.geekbrains.retrofit.servise.ProductService;
import ru.geekbrains.retrofit.util.RetrofitUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;

public class PutProductTests {
    private static ProductService productService;
    private Faker faker = new Faker();
    private Response<Product> response;
    private Integer productId;
    private Product newProduct;

    @BeforeAll
    static void beforeAll() throws MalformedURLException {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @BeforeEach
    void setUp() throws IOException {
        newProduct = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(CategoryType.ELECTRONIC.getTitle())
                .withPrice((int) (Math.random() * 1000 + 1));
        response = productService.createProductPositive(newProduct).execute();
        productId = response.body().getId();
    }

    @Test
    void updateProductPositiveTest() throws IOException {
        assert response.body() != null;
        Response<Product> updateResponse = productService
                .updateProductPositive(new Product()
                        .withId(productId)
                        .withTitle(faker.food().measurement())
                        .withPrice((int) (Math.random()*10000))
                        .withCategoryTitle(response.body().getCategoryTitle()))
                        .execute();
        assert updateResponse.body() != null;
        assertThat(response.code(), CoreMatchers.is(201));
        assertThat(updateResponse.body().getId(),
                CoreMatchers.equalTo(productId));
        assertThat(updateResponse.body().getTitle(),
                CoreMatchers.not(newProduct.getTitle()));
        assertThat(updateResponse.body().getPrice(),
                CoreMatchers.not(newProduct.getPrice()));
        assertThat(updateResponse.body().getCategoryTitle(),
                CoreMatchers.equalTo(CategoryType.ELECTRONIC.getTitle()));
    }

    @Test
    void updateProductWithBadCategoryTest() throws IOException {
        assert response.body() != null;
        Response<Product> updateResponse = productService
                .updateProductPositive(new Product()
                        .withId(productId)
                        .withTitle(faker.food().ingredient())
                        .withPrice((int) (Math.random()*-1000))
                        .withCategoryTitle(faker.cat().breed()))
                .execute();
        assertThat(updateResponse.code(), CoreMatchers
                .anyOf(CoreMatchers.is(400), CoreMatchers.is(404)));
    }

    @Test
    void updateProductNegativeTest() throws IOException {
        assert response.body() != null;
        Response<Product> updateResponse = productService
                .updateProductPositive(new Product()
                        .withTitle(faker.food().measurement())
                        .withPrice((int) (Math.random() * 10000))
                        .withCategoryTitle(response.body().getCategoryTitle()))
                .execute();
        assertThat(updateResponse.code(), CoreMatchers.is(400));
        if (updateResponse != null && !response.isSuccessful() && response.errorBody() != null) {
            ResponseBody body = response.errorBody();
            Converter<ResponseBody, ErrorBody> converter =
                    RetrofitUtils.getRetrofit().responseBodyConverter(ErrorBody.class, new Annotation[0]);
            ErrorBody errorBody = converter.convert(body);
            assertThat(errorBody.getMessage(), CoreMatchers.is("Id must be not null for new entity"));
        }
    }

    @AfterEach
    void deleteProduct(){
        try {
            if (productId != null)
                productService.deleteProduct(productId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
