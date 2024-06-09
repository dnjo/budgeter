package net.dnjo.budgeter.repositories;

import net.dnjo.budgeter.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
