package com.nodes.demo.service;

import com.nodes.demo.model.LocationNode;
import com.nodes.demo.repository.LocationNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LocationNodeService {
//
//    @Autowired
    private LocationNodeRepository repository;

    // 1. Accessing the tree or a specific node
    public List<LocationNode> getTree(Integer parentId) {
        return repository.findByParentNodeIdOrderByOrderingAsc(parentId);
    }

    // 2. Adding a node as a child to a parent
    public LocationNode addNode(Integer parentId, String title) {
        LocationNode parentNode = repository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent node not found"));

        int nextOrder = repository.findByParentNodeIdOrderByOrderingAsc(parentId).size() + 1;

        LocationNode newNode = new LocationNode();
        newNode.setTitle(title);
        newNode.setParentNode(parentNode);
        newNode.setOrdering(nextOrder);
        return repository.save(newNode);
    }

    // 3. Modifying node data
    public LocationNode updateNode(Integer nodeId, String newTitle) {
        LocationNode node = repository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Node not found"));
        node.setTitle(newTitle);
        return repository.save(node);
    }

    // 4. Deleting a node and its children
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

    // 5. Moving a node to a different parent
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

        node.setParentNode(newParent);
        node.setOrdering(nextOrder);
        return repository.save(node);
    }

    // 6. Reordering nodes within the same parent
    public void reorderNodes(Integer parentId, List<Integer> orderedNodeIds) {
        List<LocationNode> nodes = repository.findByParentNodeIdOrderByOrderingAsc(parentId);
        for (int i = 0; i < orderedNodeIds.size(); i++) {
            LocationNode node = repository.findById(orderedNodeIds.get(i))
                    .orElseThrow(() -> new RuntimeException("Node not found"));
            node.setOrdering(i + 1);
            repository.save(node);
        }
    }
}