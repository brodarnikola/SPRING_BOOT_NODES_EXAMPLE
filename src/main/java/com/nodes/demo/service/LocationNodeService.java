package com.nodes.demo.service;

import com.nodes.demo.model.LocationNode;
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

        node.setParentNodeId(null);
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

//    @Autowired
//    private LocationNodeRepository locationNodeRepository;
//
//    public LocationNode getNode(Long id) {
//        return locationNodeRepository.findById(id).orElseThrow(() -> new AppException("Node not found"));
//    }
//
//    public List<LocationNode> getTree() {
//        // Get the root node and recursively fetch the whole tree
//        LocationNode root = locationNodeRepository.findById(1L).orElseThrow(() -> new AppException("Root not found"));
//        return getChildren(root);
//    }
//
//    public LocationNode createNode(Long parentId, String name) {
//        LocationNode parent = getNode(parentId);
//        LocationNode newNode = new LocationNode();
//        newNode.setParent(parent);
//        newNode.setName(name);
//        newNode.setPosition(parent.getChildren().size() + 1); // Next free position
//        parent.getChildren().add(newNode);
//        return locationNodeRepository.save(newNode);
//    }
//
//    public LocationNode updateNode(Long id, String newName) {
//        LocationNode node = getNode(id);
//        node.setName(newName);
//        return locationNodeRepository.save(node);
//    }
//
//    public void deleteNode(Long id) {
//        LocationNode node = getNode(id);
//        if (node.getId() == 1L) {
//            throw new IllegalArgumentException("Cannot delete root node");
//        }
//        locationNodeRepository.delete(node);
//    }
//
//    public void moveNode(Long nodeId, Long newParentId) {
//        LocationNode node = getNode(nodeId);
//        LocationNode newParent = getNode(newParentId);
//
//        // Remove node from old parent
//        node.getParent().getChildren().remove(node);
//
//        // Add node to new parent
//        node.setParent(newParent);
//        node.setPosition(newParent.getChildren().size() + 1); // New free position under new parent
//        newParent.getChildren().add(node);
//
//        locationNodeRepository.save(node);
//    }
//
//    public void reorderNode(Long nodeId, Integer newPosition) {
//        LocationNode node = getNode(nodeId);
//        LocationNode parent = node.getParent();
//
//        // Recalculate positions of siblings
//        List<LocationNode> siblings = parent.getChildren();
//        siblings.remove(node);  // Temporarily remove the node
//
//        // Insert the node at the new position
//        siblings.add(newPosition - 1, node);
//
//        // Update positions of all siblings
//        for (int i = 0; i < siblings.size(); i++) {
//            siblings.get(i).setPosition(i + 1);
//        }
//
//        locationNodeRepository.saveAll(siblings);
//    }
//
//    private List<LocationNode> getChildren(LocationNode parent) {
//        // Recursively get children
//        List<LocationNode> children = locationNodeRepository.findByParent(parent);
//        for (LocationNode child : children) {
//            child.setChildren(getChildren(child));
//        }
//        return children;
//    }
}