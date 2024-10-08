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

    @Autowired
    private LocationNodeRepository repository;

    public List<LocationNode> getTree() {
        // Get the root node and recursively fetch the whole tree
        return repository.findAll();
    }

    public LocationNode addParentNode(String title) {
        LocationNode newNode = new LocationNode();
        newNode.setTitle(title);
        newNode.setParentNodeId(null);
        newNode.setOrdering(1);
        return repository.save(newNode);
    }

    public LocationNode addChildNode(Integer parentId, String title) {

        int nextOrder = repository.findByParentNodeIdOrderByOrderingAsc(parentId).size();

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