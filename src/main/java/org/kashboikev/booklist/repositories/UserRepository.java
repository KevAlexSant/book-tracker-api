package org.kashboikev.booklist.repositories;

import org.kashboikev.booklist.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
