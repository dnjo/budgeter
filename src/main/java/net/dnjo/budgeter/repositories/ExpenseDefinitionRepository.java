package net.dnjo.budgeter.repositories;

import net.dnjo.budgeter.models.ExpenseDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseDefinitionRepository extends JpaRepository<ExpenseDefinition, Long> {

}
