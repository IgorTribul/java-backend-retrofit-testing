package ru.geekbrains.retrofit.servise;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.geekbrains.retrofit.dto.Category;

public interface CategoryService {

    @GET("categories/{id}")
    Call<Category> getCategoryById(@Path("id") Integer id);

    @GET("categories")
    Call<Category> getAllCategories();

    @GET("categories/{id}")
    Call<Category> getCategoryByStringId(@Path("id") String id);
}
