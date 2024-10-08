package com.nodes.demo.service;

import com.nodes.demo.model.LocationNode;
import com.nodes.demo.model.request.ReorderRequest;
import com.nodes.demo.repository.LocationNodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationNodeService {
//

    private static final Logger logger = LoggerFactory.getLogger(LocationNodeService.class);

    @Autowired
    private LocationNodeRepository repository;

   public List<LocationNode> getTree() {
        // Get the root node and recursively fetch the whole tree
       return repository.findAll();
    }

    // 1. Accessing the tree or a specific node
    public List<LocationNode> getTree(Integer parentId) {
        return repository.findByParentNodeIdOrderByOrderingAsc(parentId);
    }

    // 2. Adding a node as a child to a parent
//    public LocationNode addNode(Integer parentId, String title) {
//        LocationNode parentNode = repository.findById(parentId)
//                .orElseThrow(() -> new RuntimeException("Parent node not found"));
//
//        int nextOrder = repository.findByParentNodeIdOrderByOrderingAsc(parentId).size() + 1;
//
//        LocationNode newNode = new LocationNode();
//        newNode.setTitle(title);
//        newNode.setParentNode(parentNode);
//        newNode.setOrdering(nextOrder);
//        return repository.save(newNode);
//    }

    public LocationNode addParentNode(  String title) {
//        LocationNode parentNode = repository.findById(parentId)
//                .orElseThrow(() -> new RuntimeException("Parent node not found"));

        logger.debug("title list data is - {}", title);

//        LocationNode parentNode = repository.findById(parentId).orElse(new LocationNode());

//        int nextOrder = repository.findByParentNodeIdOrderByOrderingAsc(parentId).size() + 1;

        logger.debug("parentNode list data is - {}", new LocationNode());

        LocationNode newNode = new LocationNode();
        newNode.setTitle(title);
        newNode.setParentNodeId(null);
        newNode.setOrdering(1);
        return repository.save(newNode);
    }

    public LocationNode addChildNode(Integer parentId,  String title) {
//        LocationNode parentNode = repository.findById(parentId)
//                .orElseThrow(() -> new RuntimeException("Parent node not found"));

        logger.debug("title list data is - {}", title);

//        LocationNode parentNode = repository.findById(parentId).orElse(new LocationNode());

        int nextOrder = repository.findByParentNodeIdOrderByOrderingAsc(parentId).size();

        logger.debug("parentNode list data is - {}", new LocationNode());

        LocationNode newNode = new LocationNode();
        newNode.setTitle(title);
        newNode.setParentNodeId(parentId);
        newNode.setOrdering(nextOrder);
        return repository.save(newNode);
    }

    public LocationNode updateNode(Integer nodeId, String newTitle) {
        LocationNode node = repository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Node not found"));
        node.setTitle(newTitle);
        return repository.save(node);
    }

    public void deleteNode(Integer nodeId) {
        LocationNode node = repository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Node not found"));
        if (node.getId() == 1) {
            throw new RuntimeException("Root node cannot be deleted");
        }

        deleteRecursively(node);
    }

    private void deleteRecursively(LocationNode node) {
        List<LocationNode> children = repository.findByParentNodeIdOrderByOrderingAsc(node.getId());
        for (LocationNode child : children) {
            deleteRecursively(child);
        }
        repository.delete(node);
    }

    public LocationNode moveNode(Integer nodeId, Integer newParentId) {
        LocationNode node = repository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Node not found"));
        LocationNode newParent = repository.findById(newParentId)
                .orElseThrow(() -> new RuntimeException("New parent node not found"));

        // Prevent moving root or under itself
        if (node.getId() == 1 || nodeId.equals(newParentId)) {
            throw new RuntimeException("Invalid move operation");
        }

        int nextOrder = repository.findByParentNodeIdOrderByOrderingAsc(newParentId).size() + 1;

        node.setParentNodeId(null);
        node.setOrdering(nextOrder);
        return repository.save(node);
    }

    public void reorderNodes(List<ReorderRequest> reorderRequests) {
        for (ReorderRequest reorderRequest : reorderRequests) {
            LocationNode node = repository.findById(reorderRequest.getId())
                    .orElseThrow(() -> new RuntimeException("Node not found"));
            node.setParentNodeId(reorderRequest.getParentNodeId());
            node.setOrdering(reorderRequest.getOrdering());
            repository.save(node);
        }
    }

}