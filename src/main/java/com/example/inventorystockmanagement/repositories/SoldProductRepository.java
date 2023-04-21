package com.example.inventorystockmanagement.repositories;


import com.example.inventorystockmanagement.entities.SoldProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SoldProductRepository extends JpaRepository<SoldProduct,Long>{

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM sold_products where id =: sp_id)", nativeQuery = true)
	void deleteWithRawSqlQuery(@Param("sp_id") Long sp_id);
	
}
