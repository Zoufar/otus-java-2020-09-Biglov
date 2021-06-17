package hw28W_springDataJdbc.crm.repository;


import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import hw28W_springDataJdbc.crm.model.ClientBD;

import java.util.Optional;


public interface ClientBDRepository extends CrudRepository<ClientBD, Long> {

    @Query("select * from client where name = :name")
    Optional<ClientBD> findByName(@Param("name") String name);

    @Modifying
    @Query("update client set name = :newName where id = :id")
    void updateName(@Param("id") long id, @Param("newName") String newName);
}
