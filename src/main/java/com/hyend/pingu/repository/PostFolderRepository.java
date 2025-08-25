package com.hyend.pingu.repository;

import com.hyend.pingu.entity.PostFolder;
import com.hyend.pingu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostFolderRepository extends JpaRepository<PostFolder, Long> {

    List<PostFolder> findByUser(User user);
}
