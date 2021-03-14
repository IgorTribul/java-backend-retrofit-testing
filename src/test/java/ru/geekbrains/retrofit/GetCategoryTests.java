package ru.geekbrains.retrofit;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.retrofit.dto.Category;
import ru.geekbrains.retrofit.dto.Product;
import ru.geekbrains.retrofit.servise.CategoryService;
import ru.geekbrains.retrofit.util.DbUtils;
import ru.geekbrains.retrofit.util.RetrofitUtils;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetCategoryTests {

    private List<Product> products = new ArrayList<>();
    private static CategoryService categoryService;
    private Response<Category> response;

    @BeforeAll
    static void beforeAll() throws MalformedURLException {
       categoryService = RetrofitUtils.getRetrofit().create(CategoryService.class);
    }

    @Feature(value = "Получение категории продуктов")
    @Story(value = "Positive tests")
    @Test
    @Description(value = "Получение категории FOOD")
    void getCategoryByIdTest() throws IOException {
        response = categoryService.getCategoryById(CategoryType.FOOD.getId()).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getTitle(), CoreMatchers.is(CategoryType.FOOD.getTitle()));
        assertThat(response.body().getProducts(), CoreMatchers.notNullValue());
        products.addAll(response.body().getProducts());
        for (int i = 0; i< products.size(); i++){
            assertThat(products.get(i).getCategoryTitle(), CoreMatchers.is(CategoryType.FOOD.getTitle()));
        }
        assertThat(response.body().getId(), CoreMatchers.is(DbUtils.getCategoriesMapper().selectByPrimaryKey(1).getId()));
    }

    @Feature(value = "Получение категории продуктов")
    @Story(value = "Positive tests")
    @Test
    @Description(value = "Получение категории ELECTRONIC")
    void getCategoryByIdTest2() throws IOException {
        response = categoryService.getCategoryById(CategoryType.ELECTRONIC.getId()).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getTitle(), CoreMatchers.is(CategoryType.ELECTRONIC.getTitle()));
        assertThat(response.body().getProducts(), CoreMatchers.notNullValue());
        products.addAll(response.body().getProducts());
        for (int i = 0; i< products.size(); i++){
            assertThat(products.get(i).getCategoryTitle(), CoreMatchers.is(CategoryType.ELECTRONIC.getTitle()));
        }
    }

    @Feature(value = "Получение категории продуктов")
    @Story(value = "Negative tests")
    @Test
    @Description(value = "Попытка получить все категории сразу")
    void getAllCategoriesTest() throws IOException {
        response = categoryService.getAllCategories().execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(404));
        assert response.errorBody() != null;
        assertThat(response.errorBody().string(),
                CoreMatchers.containsString("Not Found"));
    }

    @Feature(value = "Получение категории продуктов")
    @Story(value = "Negative tests")
    @Test
    @Description(value = "Попытка получить категорию с несуществующим ID")
    void getCategoryByNotFoundIdTest() throws IOException {
        response = categoryService
                .getCategoryById((int) (Math.random() * 1000) +10).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(404));
        assert response.errorBody() != null;
        assertThat(response.errorBody().string(),
                CoreMatchers.containsString("Unable to find category with id:"));
    }

    @Feature(value = "Получение категории продуктов")
    @Story(value = "Negative tests")
    @Test
    @Description(value = "Попытка получить категорию с некорректным ID")
    void getCategoryByStringIdTest() throws IOException {
        response = categoryService
                .getCategoryByStringId(new Faker().beer().name()).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(400));
        assert response.errorBody() != null;
        assertThat(response.errorBody().string(),
                CoreMatchers.containsString("Bad Request"));
    }
}
