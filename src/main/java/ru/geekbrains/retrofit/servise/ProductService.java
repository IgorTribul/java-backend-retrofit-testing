package ru.geekbrains.retrofit.servise;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import ru.geekbrains.retrofit.dto.Product;

public interface ProductService {

    @GET("products")
    Call<Product[]> getProducts();

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") Integer id);

    @GET("products/{id}")
    Call<Product> getProductByString(@Path("id") String id);

    @POST("products")
    Call<Product> createProductPositive(@Body Product newProduct);

    @POST("products")
    Call<ResponseBody> createProductNegative(@Body Product newProduct);

    @DELETE("products")
    Call<ResponseBody> deleteProductWithoutId();

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") Integer id);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProductWithStringId(@Path("id") String string);

    @PUT("products")
    Call<Product> updateProductPositive(@Body Product product);
}
