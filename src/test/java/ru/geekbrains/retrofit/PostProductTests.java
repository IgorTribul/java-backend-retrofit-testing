package ru.geekbrains.retrofit;

import com.github.javafaker.Faker;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.mybatis.dao.ProductsMapper;
import ru.geekbrains.retrofit.dto.Product;
import ru.geekbrains.retrofit.servise.ProductService;
import ru.geekbrains.retrofit.util.DbUtils;
import ru.geekbrains.retrofit.util.RetrofitUtils;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;

public class PostProductTests {
    private static ProductService productService;
    private Integer productId;
    private Response<ResponseBody> bodyResponse;
    private Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() throws MalformedURLException {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @Test
    void createProductPositiveTest() throws IOException {
        Response<Product> response = productService
                .createProductPositive(new PreparedData().getNewProduct()).execute();
        productId = response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getCategoryTitle(), CoreMatchers.is(CategoryType.FOOD.getTitle()));
        assertThat(response.body().getPrice(), CoreMatchers.notNullValue());
        assertThat(response.code(), CoreMatchers.is(201));
    }

    @Test
    void createProductWithIdNegativeTest() throws IOException {
        bodyResponse = productService
                .createProductNegative(new PreparedData().getNewProduct()
                        .withId((int) (Math.random() * 1000))).execute();
        assertThat(bodyResponse.isSuccessful(), CoreMatchers.is(false));
        assertThat(bodyResponse.code(), CoreMatchers.is(400));
        assertThat(bodyResponse.errorBody().string(), CoreMatchers.containsString("Id must be null for new entity"));
    }

    @Test
    void createProductWithFakeCategoryTitleTest() throws IOException {
        bodyResponse = productService
                .createProductNegative(new Product().withCategoryTitle(faker.food().spice())).execute();
        assertThat(bodyResponse.isSuccessful(), CoreMatchers.is(false));
        assertThat(bodyResponse.code(), CoreMatchers
                .anyOf(CoreMatchers.is(400), CoreMatchers.is(404)));
    }

    @Test
    void createProductWithEmptyBodyTest() throws IOException {
        bodyResponse = productService
                .createProductNegative(new Product()).execute();
        assertThat(bodyResponse.isSuccessful(), CoreMatchers.is(false));
        assertThat(bodyResponse.code(), CoreMatchers
                .anyOf(CoreMatchers.is(400), CoreMatchers.is(404)));
    }

    @AfterEach
    void deleteProduct() throws IOException {
        ProductsMapper productsMapper = DbUtils.getProductsMapper();
        if (productId != null) {
            productsMapper.deleteByPrimaryKey(Long.valueOf(productId));
            assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)), CoreMatchers.nullValue());
        }
    }
}
