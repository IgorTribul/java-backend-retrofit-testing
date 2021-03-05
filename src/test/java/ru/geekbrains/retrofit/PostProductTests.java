package ru.geekbrains.retrofit;

import com.github.javafaker.Faker;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.retrofit.CategoryType;
import ru.geekbrains.retrofit.dto.Product;
import ru.geekbrains.retrofit.servise.ProductService;
import ru.geekbrains.retrofit.util.RetrofitUtils;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;

public class PostProductTests {
    private static ProductService productService;
    private Product newProduct;
    private Integer prodId;
    private Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() throws MalformedURLException {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @BeforeEach
    void newProductBuild(){
        newProduct= new Product()
                .withPrice((int) (Math.random()*10000))
                .withCategoryTitle(CategoryType.ELECTRONIC.getTitle())
                .withTitle(faker.company().name());
    }

    @Test
    void createProductPositiveTest() throws IOException {
        Response<Product> response = productService.createProductPositive(newProduct).execute();
        prodId = response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getCategoryTitle(), CoreMatchers.is(CategoryType.ELECTRONIC.getTitle()));
        assertThat(response.body().getPrice(), CoreMatchers.notNullValue());
        assertThat(response.code(), CoreMatchers.is(201));
    }

    @Test
    void createProductWithIdNegativeTest() throws IOException {
        Response<ResponseBody> response = productService
                .createProductNegative(newProduct.withId((int) (Math.random() * 1000))).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(400));
        assertThat(response.errorBody().string(), CoreMatchers.containsString("Id must be null for new entity"));
    }

    @Test
    void createProductWithFakeCategoryTitleTest() throws IOException {
        Response<ResponseBody> response = productService
                .createProductNegative(new Product().withCategoryTitle(faker.food().spice())).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers
                .anyOf(CoreMatchers.is(400), CoreMatchers.is(404)));
    }

    @Test
    void createProductWithEmptyBodyTest() throws IOException {
        Response<ResponseBody> response = productService
                .createProductNegative(new Product()).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers
                .anyOf(CoreMatchers.is(400), CoreMatchers.is(404)));
    }

    @AfterEach
    void deleteProduct(){
        try {
            if (prodId != null)
            productService.deleteProduct(prodId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
