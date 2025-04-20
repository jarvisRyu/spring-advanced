package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @EntityGraph(attributePaths = {"user"})
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    /*N+1 : 쿼리가 해당객체가 가지고있는 연관관계 또한 조회하게 됨.
    * JOIN FETCH 를 통해 N+1문제 해결한상태
    * 연관된 엔티티를 즉시 로딩*/

//            "LEFT JOIN FETCH t.user " +
//            "WHERE t.id = :todoId")
    @EntityGraph(attributePaths ={"user"})
    Optional<Todo> findByIdWithUser(Long todoId);

    int countById(Long todoId);
}
