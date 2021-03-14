package ru.geekbrains.retrofit;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.mybatis.dao.ProductsMapper;
import ru.geekbrains.mybatis.model.ProductsExample;
import ru.geekbrains.retrofit.dto.Product;
import ru.geekbrains.retrofit.servise.ProductService;
import ru.geekbrains.retrofit.util.DbUtils;
import ru.geekbrains.retrofit.util.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetProductTests {
    private static ProductService productService;
    private static ProductsMapper productsMapper;
    private Faker faker = new Faker();
    private Response<Product> response;
    private Integer productId;
    ProductsExample example = new ProductsExample();

    @BeforeAll
    static void beforeAll() throws IOException {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
        productsMapper = DbUtils.getProductsMapper();
    }

    @BeforeEach
    void setUp() throws IOException {
        response = productService.createProductPositive(new PreparedData()
                .getNewProduct()).execute();
        assert response.body() != null;
        productId = response.body().getId();
    }

    @Feature(value = "Получение продукта")
    @Story(value = "Positive tests")
    @Test
    @Description(value = "Получение всех продуктов всех категорий")
    void getAllProductsTest() throws IOException {
        Response<Product[]>response2 = productService.getProducts().execute();
        assertThat(response2.isSuccessful(), CoreMatchers.is(true));
        assertThat(response2.body(), CoreMatchers.notNullValue());
        assertThat(response2.code(), CoreMatchers.is(200));
    }

    @Feature(value = "Получение продукта")
    @Story(value = "Positive tests")
    @Test
    @Description(value = "Получение всех продуктов всех категорий")
    void getProductByIdTest() throws IOException {
        response = productService.getProductById(productId).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assert response.body() != null;
        assertThat(response.body().getId(), CoreMatchers.is(productId));
        assertThat(response.code(), CoreMatchers.is(200));
        assertThat(response.body().getPrice(),
                CoreMatchers.is(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getPrice()));

    }

    @Feature(value = "Получение продукта")
    @Story(value = "Negative tests")
    @Test
    @Description(value = "Попытка получить продукт с несуществующим ID")
    void getProductByNotFoundIdTest() throws IOException {
        response = productService.getProductById((int) (Math.random()*1000)+10000).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(404));
        assert response.errorBody() != null;
        assertThat(response.errorBody().string(),
                CoreMatchers.containsString("Unable to find product with id:"));
    }

    @Feature(value = "Получение продукта")
    @Story(value = "Negative tests")
    @Test
    @Description(value = "Попытка получить продукт с некорректным ID")
    void getProductByStringIdTest() throws IOException {
        response = productService.getProductByString(faker.beer().name()).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(400));
        assert response.errorBody() != null;
        assertThat(response.errorBody().string(),
                CoreMatchers.containsString("Bad Request"));
    }

    @AfterEach
    void tearDown() throws IOException {
        if (productId != null) {
            productsMapper.deleteByPrimaryKey(Long.valueOf(productId));
            assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)), CoreMatchers.nullValue());
        }
    }
}

