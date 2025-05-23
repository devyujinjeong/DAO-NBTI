package com.dao.nbti.problem.domain.repository;

import com.dao.nbti.problem.domain.aggregate.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // 상위 카테고리가 없는 (최상위) 카테고리만 조회
    @Query(value = "SELECT * FROM category WHERE parent_category_id IS NULL", nativeQuery = true)
    List<Category> findByParentCategoryIsNull();

    List<Category> findByParentCategoryIdIsNullOrderByCategoryIdAsc();

    List<Category> findByParentCategoryIdIsNotNullOrderByParentCategoryIdAscCategoryIdAsc();

    // 하위 카테고리의 상위 카테고리 조회
    @Query("SELECT c.parentCategoryId FROM Category c WHERE c.categoryId = :categoryId")
    int findParentCategoryIdByCategoryId(int categoryId);

}
