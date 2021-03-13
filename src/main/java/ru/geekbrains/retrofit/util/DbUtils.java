package ru.geekbrains.retrofit.util;

import lombok.experimental.UtilityClass;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import ru.geekbrains.mybatis.dao.CategoriesMapper;
import ru.geekbrains.mybatis.dao.ProductsMapper;

import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class DbUtils {

    public CategoriesMapper getCategoriesMapper() throws IOException {
        return getSession().getMapper(CategoriesMapper.class);
    }

    public ProductsMapper getProductsMapper() throws IOException {
        return getSession().getMapper(ProductsMapper.class);
    }

    public SqlSession getSession() throws IOException {
        String resource = "mybatisConfig.xml";
        InputStream stream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(stream);
        return sessionFactory.openSession(true);
    }
}
