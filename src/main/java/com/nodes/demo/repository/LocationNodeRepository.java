package com.nodes.demo.repository;

import com.nodes.demo.model.LocationNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationNodeRepository extends JpaRepository<LocationNode, Long> {

//    List<LocationNode> findByParent(LocationNode parent);

    List<LocationNode> findByParentNodeIdOrderByOrderingAsc(Integer parentId);
    Optional<LocationNode> findById(Integer id);
}
