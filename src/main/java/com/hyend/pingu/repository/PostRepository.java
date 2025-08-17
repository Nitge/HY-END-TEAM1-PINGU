package com.hyend.pingu.repository;

import com.hyend.pingu.entity.Post;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUserId(Long userId, Pageable pageable);

    @Query(
            value = "SELECT * FROM Post p WHERE ST_Distance_Sphere(p.location, :point) <= :distance",
            nativeQuery = true
    )
    List<Post> findNear(@Param("point") Point point, @Param("distance") Double distance);
}
