package ru.geekbrains.retrofit;

import com.github.javafaker.Faker;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.mybatis.dao.ProductsMapper;
import ru.geekbrains.retrofit.dto.Product;
import ru.geekbrains.retrofit.servise.ProductService;
import ru.geekbrains.retrofit.util.DbUtils;
import ru.geekbrains.retrofit.util.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class DeleteProductTests {

    private static ProductService productService;
    private static ProductsMapper productsMapper;
    private Integer productId;
    private Faker faker = new Faker();
    private Response<ResponseBody> bodyResponse;

    @BeforeAll
    static void beforeAll() throws IOException {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
        productsMapper = DbUtils.getProductsMapper();
    }

    @BeforeEach
    void setUp() throws IOException {
        Response<Product> response = productService.createProductPositive(new PreparedData().getNewProduct()).execute();
        productId = response.body().getId();
        String title = response.body().getTitle();
        Integer price = response.body().getPrice();
        String categoryTitle = response.body().getCategoryTitle();
        assertThat(price, CoreMatchers.is(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getPrice()));
        assertThat(title, CoreMatchers.is(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getTitle()));
        assertThat(categoryTitle, CoreMatchers.is("Food"));
     }

    @Test
    void deleteProductPositiveTest() throws IOException {
        bodyResponse = productService.deleteProduct(productId).execute();
        assertThat(bodyResponse.isSuccessful(), CoreMatchers.is(true));
        assertThat(bodyResponse.code(), CoreMatchers.is(200));
        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getPrice(), CoreMatchers.nullValue());
    }

    @Test
    void deleteProductThroughBdTest() throws IOException {
        productsMapper.deleteByPrimaryKey(Long.valueOf(productId));
        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)), CoreMatchers.nullValue());
    }

    @Test
    void deleteProductRepeatTest() throws IOException {
        productService.deleteProduct(productId).execute();
        bodyResponse = productService
                .deleteProduct(productId).execute();
        System.out.println(bodyResponse.code());
        assertThat(bodyResponse.isSuccessful(), CoreMatchers.is(true));
        assertThat(bodyResponse.code(), CoreMatchers
                .anyOf(CoreMatchers.is(200), CoreMatchers.is(204)));
    }

    @Test
    void deleteProductByNotFoundIdTest() throws IOException {
        bodyResponse = productService
                .deleteProduct((int) (Math.random()*10000)+10000).execute();
        assertThat(bodyResponse.isSuccessful(), CoreMatchers.is(false));
        assertThat(bodyResponse.code(), CoreMatchers
                .anyOf(CoreMatchers.is(400), CoreMatchers.is(404)));
    }

    @Test
    void deleteProductWithoutIdTest() throws IOException {
        bodyResponse = productService.deleteProductWithoutId().execute();
        assertThat(bodyResponse.isSuccessful(), CoreMatchers.is(false));
        assertThat(bodyResponse.code(), CoreMatchers.is(400));
    }

    @Test
    void deleteProductWitStringIdTest() throws IOException {
        bodyResponse = productService
                .deleteProductWithStringId(faker.cat().name()).execute();
        assertThat(bodyResponse.isSuccessful(), CoreMatchers.is(false));
        assertThat(bodyResponse.code(), CoreMatchers.is(400));
    }

    @AfterEach
    void tearDown() throws IOException {
        ProductsMapper productsMapper = DbUtils.getProductsMapper();
        if (productId != null) {
            productsMapper.deleteByPrimaryKey(Long.valueOf(productId));
            assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)), CoreMatchers.nullValue());
        }
    }
}
