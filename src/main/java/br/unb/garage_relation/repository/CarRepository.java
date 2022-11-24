package br.unb.garage_relation.repository;

import br.unb.garage_relation.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    @Query("FROM Car c WHERE c.brand LIKE CONCAT('%',:query,'%') OR c.model LIKE CONCAT('%',:query,'%')")
    List<Car> findByBrandOrModelContains(@Param("query") String query);
}