package com.app.repo;

import com.app.model.ApplicationComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationCommentRepo extends JpaRepository<ApplicationComment, Long> {
}
