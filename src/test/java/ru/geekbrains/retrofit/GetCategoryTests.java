package ru.geekbrains.retrofit;

import com.github.javafaker.Faker;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.retrofit.CategoryType;
import ru.geekbrains.retrofit.dto.Category;
import ru.geekbrains.retrofit.dto.Product;
import ru.geekbrains.retrofit.servise.CategoryService;
import ru.geekbrains.retrofit.util.RetrofitUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetCategoryTests {

    private List<Product> products = new ArrayList<>();
    private static CategoryService categoryService;

    @BeforeAll
    static void beforeAll() throws MalformedURLException {
       categoryService = RetrofitUtils.getRetrofit().create(CategoryService.class);
    }

    @Test
    void getCategoryByIdTest() throws IOException {
        Response<Category> response = categoryService.getCategoryById(CategoryType.FOOD.getId()).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getTitle(), CoreMatchers.is(CategoryType.FOOD.getTitle()));
        assertThat(response.body().getProducts(), CoreMatchers.notNullValue());
        products.addAll(response.body().getProducts());
        for (int i = 0; i< products.size(); i++){
            assertThat(products.get(i).getCategoryTitle(), CoreMatchers.is(CategoryType.FOOD.getTitle()));
        }
    }

    @Test
    void getCategoryByIdTest2() throws IOException {
        Response<Category> response = categoryService.getCategoryById(CategoryType.ELECTRONIC.getId()).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getTitle(), CoreMatchers.is(CategoryType.ELECTRONIC.getTitle()));
        assertThat(response.body().getProducts(), CoreMatchers.notNullValue());
        products.addAll(response.body().getProducts());
        for (int i = 0; i< products.size(); i++){
            assertThat(products.get(i).getCategoryTitle(), CoreMatchers.is(CategoryType.ELECTRONIC.getTitle()));
        }
    }
    @Test
    void getAllCategoriesTest() throws IOException {
        Response<Category> response = categoryService.getAllCategories().execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(404));
        assert response.errorBody() != null;
        assertThat(response.errorBody().string(),
                CoreMatchers.containsString("Not Found"));
    }
    @Test
    void getCategoryByNotFoundIdTest() throws IOException {
        Response<Category> response = categoryService
                .getCategoryById((int) (Math.random() * 1000) +10).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(404));
        assert response.errorBody() != null;
        assertThat(response.errorBody().string(),
                CoreMatchers.containsString("Unable to find category with id:"));
    }
    @Test
    void getCategoryByStringIdTest() throws IOException {
        Response<Category> response = categoryService
                .getCategoryByStringId(new Faker().beer().name()).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(400));
        assert response.errorBody() != null;
        assertThat(response.errorBody().string(),
                CoreMatchers.containsString("Bad Request"));
    }
}
