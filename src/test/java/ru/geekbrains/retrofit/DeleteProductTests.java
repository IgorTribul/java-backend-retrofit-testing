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
import ru.geekbrains.retrofit.CategoryType;
import ru.geekbrains.retrofit.dto.Product;
import ru.geekbrains.retrofit.servise.ProductService;
import ru.geekbrains.retrofit.util.DbUtils;
import ru.geekbrains.retrofit.util.RetrofitUtils;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;

public class DeleteProductTests {

    private static ProductService productService;
    private Integer productId;
    private Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() throws MalformedURLException {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @BeforeEach
    void setUp() throws IOException {
        Product newProduct = new Product()
                .withTitle(faker.food().ingredient())
                .withPrice((int) (Math.random()*1000+1))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProductPositive(newProduct).execute();
        productId = response.body().getId();
        String title = response.body().getTitle();
        Integer price = response.body().getPrice();
        String categoryTitle = response.body().getCategoryTitle();
        assertThat(price, CoreMatchers.is(DbUtils.getProductsMapper().selectByPrimaryKey(Long.valueOf(productId)).getPrice()));
        assertThat(title, CoreMatchers.is(DbUtils.getProductsMapper().selectByPrimaryKey(Long.valueOf(productId)).getTitle()));
        assertThat(categoryTitle, CoreMatchers.is("Food"));
     }

    @Test
    void deleteProductPositiveTest() throws IOException {
        Response<ResponseBody> response = productService.deleteProduct(productId).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), CoreMatchers.is(200));
        assertThat(DbUtils.getProductsMapper().selectByPrimaryKey(Long.valueOf(productId)), CoreMatchers.nullValue());
    }
//
//    @Test
//    void deleteProductRepeatTest() throws IOException {
//        productService.deleteProduct(productId).execute();
//        Response<ResponseBody> response = productService
//                .deleteProduct(productId).execute();
//        assertThat(response.isSuccessful(), CoreMatchers.is(true));
//        assertThat(response.code(), CoreMatchers
//                .anyOf(CoreMatchers.is(200), CoreMatchers.is(204)));
//    }
//
    @Test
    void deleteProductByNotFoundIdTest() throws IOException {
        Response<ResponseBody> response = productService
                .deleteProduct((int) (Math.random()*10000)+10000).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers
                .anyOf(CoreMatchers.is(400), CoreMatchers.is(404)));
    }
//
//    @Test
//    void deleteProductWithoutIdTest() throws IOException {
//        Response<ResponseBody> response = productService.deleteProductWithoutId().execute();
//        assertThat(response.isSuccessful(), CoreMatchers.is(false));
//        assertThat(response.code(), CoreMatchers.is(400));
//    }
//
//    @Test
//    void deleteProductWitStringIdTest() throws IOException {
//        Response<ResponseBody> response = productService
//                .deleteProductWithStringId(faker.cat().name()).execute();
//        assertThat(response.isSuccessful(), CoreMatchers.is(false));
//        assertThat(response.code(), CoreMatchers.is(400));
//    }

    @AfterEach
    void tearDown() throws IOException {
        if (productId != null){
            DbUtils.getProductsMapper().deleteByPrimaryKey(Long.valueOf(productId));
            assertThat(DbUtils.getProductsMapper().selectByPrimaryKey(Long.valueOf(productId)), CoreMatchers.nullValue());
        }
    }
}
