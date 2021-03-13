package ru.geekbrains.retrofit;

import com.github.javafaker.Faker;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import ru.geekbrains.mybatis.dao.CategoriesMapper;
import ru.geekbrains.mybatis.dao.ProductsMapper;
import ru.geekbrains.mybatis.model.Categories;
import ru.geekbrains.mybatis.model.Products;
import ru.geekbrains.mybatis.model.ProductsExample;
import ru.geekbrains.retrofit.util.DbUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InsertUpdateTests {
    private static ProductsMapper productsMapper;
    private static CategoriesMapper categoriesMapper;
    private static Long productId;
    private static Integer categoryId;
    Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() throws IOException {
        categoriesMapper = DbUtils.getCategoriesMapper();
        productsMapper = DbUtils.getProductsMapper();
    }

    @Order(1)
    @Test
    void insertNewProduct() throws IOException {
        Products newProductToInsert = new PreparedData().getNewProducts();
        productsMapper.insert(newProductToInsert);
        productId = newProductToInsert.getId();
        assertThat(newProductToInsert.getTitle(),
                CoreMatchers.equalTo(productsMapper.selectByPrimaryKey(productId).getTitle()));
        assertThat(newProductToInsert.getPrice(),
                CoreMatchers.equalTo(productsMapper.selectByPrimaryKey(productId).getPrice()));
    }

    @Order(2)
    @Test
    void insertNewCategory() throws IOException {
        Categories newCategoryToInsert = new Categories();
        newCategoryToInsert.setTitle(faker.book().author());
        categoriesMapper.insert(newCategoryToInsert);
        categoryId = newCategoryToInsert.getId();
        assertThat(newCategoryToInsert.getTitle(),
                CoreMatchers.equalTo(categoriesMapper.selectByPrimaryKey(categoryId).getTitle()));
    }

    @Order(3)
    @Test
    void updateInsertedProduct() throws IOException {
        ProductsExample productsExample = new ProductsExample();
        productsExample.createCriteria().andIdEqualTo(productId);
        Products insertedProduct = productsMapper.selectByPrimaryKey(productId);
        System.out.println("title до обновления:  " + insertedProduct.getTitle());
        System.out.println("category_id до обновления:  " + insertedProduct.getCategory_id());
        insertedProduct.setTitle(faker.food().spice());
        insertedProduct.setCategory_id(Long.valueOf(categoryId));
        productsMapper.updateByExample(insertedProduct, productsExample);
        assertThat(insertedProduct.getTitle(), CoreMatchers.equalTo(productsMapper.selectByPrimaryKey(productId).getTitle()));
        System.out.println("title для обновления:  "+insertedProduct.getTitle()+" и он соответствует сохраненному в БД:  "+productsMapper.selectByPrimaryKey(productId).getTitle());
        System.out.println("category_id для обновления:  "+insertedProduct.getCategory_id()+" и он соответствует сохраненному в БД:  "+productsMapper.selectByPrimaryKey(productId).getCategory_id());
    }

    @AfterAll
    static void afterAll() {
        if (productId !=null || categoryId !=null) {
            productsMapper.deleteByPrimaryKey(productId);
            categoriesMapper.deleteByPrimaryKey(categoryId);
            assertThat(productsMapper.selectByPrimaryKey(productId), CoreMatchers.nullValue());
            assertThat(categoriesMapper.selectByPrimaryKey(categoryId), CoreMatchers.nullValue());
        }
    }
}
