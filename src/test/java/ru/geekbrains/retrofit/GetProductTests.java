package ru.geekbrains.retrofit;

import com.github.javafaker.Faker;
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

public class GetProductTests {
    private static ProductService productService;
    private Faker faker = new Faker();
    private Response<Product> response;
    private Integer productId;

    @BeforeAll
    static void beforeAll() throws MalformedURLException {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @BeforeEach
    void setUp() throws IOException {
        Product newProductRequest = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(CategoryType.FOOD.getTitle())
                .withPrice((int) (Math.random() * 1000 + 1));
        response = productService.createProductPositive(newProductRequest).execute();
        productId = response.body().getId();
    }

    @Test
    void getAllProductsTest() throws IOException {
        Response<Product[]>response2 = productService.getProducts().execute();
        assertThat(response2.isSuccessful(), CoreMatchers.is(true));
        assertThat(response2.body(), CoreMatchers.notNullValue());
        assertThat(response2.code(), CoreMatchers.is(200));
    }

    @Test
    void getProductByIdTest() throws IOException {
        response = productService.getProductById(productId).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assert response.body() != null;
        assertThat(response.body().getId(), CoreMatchers.is(productId));
        assertThat(response.code(), CoreMatchers.is(200));
    }

    @Test
    void getProductByNotFoundIdTest() throws IOException {
        response = productService.getProductById((int) (Math.random()*1000)+10000).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(404));
        assert response.errorBody() != null;
        assertThat(response.errorBody().string(),
                CoreMatchers.containsString("Unable to find product with id:"));
    }

    @Test
    void getProductByStringIdTest() throws IOException {
        response = productService.getProductByString(faker.beer().name()).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(400));
        assertThat(response.errorBody().string(),
                CoreMatchers.containsString("Bad Request"));
    }

    @AfterEach
    void tearDown() {
        if (productId != null)
        try {
            productService.deleteProduct(productId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

