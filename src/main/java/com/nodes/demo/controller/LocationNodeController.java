package com.nodes.demo.controller;

import com.nodes.demo.model.LocationNode;
import com.nodes.demo.service.LocationNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nodes")
public class LocationNodeController {

    private static final Logger logger = LoggerFactory.getLogger(LocationNodeController.class);

    @Autowired
    private LocationNodeService service;

    // 1. Get the whole tree
    @GetMapping("/tree")
    public ResponseEntity<List<LocationNode>> getTree() {
        List<LocationNode> listNodes = service.getTree();

        logger.debug("Responding list size is - {}", listNodes.size());
        logger.debug("Responding list data is - {}", listNodes);

        return ResponseEntity.ok(listNodes);
    }

    @PostMapping("/addParent/{title}")
//    public ResponseEntity<LocationNode> addNode(  @RequestParam(defaultValue = "b", name = "title", value = "title") String title) {
    public ResponseEntity<LocationNode> addParentNode( @PathVariable String title) {

        try {
            logger.debug("Responding list data is - {}", title);
            return ResponseEntity.ok(service.addParentNode( title));
        } catch (Exception e) {

            logger.error("exception getLocalizedMessage is - {}", e.getLocalizedMessage());
            logger.error("exception getCause is - {}", e.getCause());
            return ResponseEntity.ok(service.addParentNode( title));
        }
    }

    @PostMapping("/addChild/{parentId}/{title}")
    public ResponseEntity<LocationNode> addChildNode( @PathVariable Integer parentId, @PathVariable String title) {

        try {
            logger.debug("Responding parentId data is - {}", parentId);
            logger.debug("Responding title data is - {}", title);
            return ResponseEntity.ok(service.addChildNode(parentId, title));
        } catch (Exception e) {

            logger.error("exception getLocalizedMessage is - {}", e.getLocalizedMessage());
            logger.error("exception getCause is - {}", e.getCause());
            return ResponseEntity.ok(service.addChildNode(parentId, title));
        }
    }

    // 3. Update node
    @PutMapping("/{nodeId}")
    public ResponseEntity<LocationNode> updateNode(@PathVariable Integer nodeId, @RequestParam String title) {
        return ResponseEntity.ok(service.updateNode(nodeId, title));
    }

    // 4. Delete node
    @DeleteMapping("/{nodeId}")
    public ResponseEntity<Void> deleteNode(@PathVariable Integer nodeId) {
        if(nodeId != 1) {
            service.deleteNode(nodeId);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 5. Move node
    @PutMapping("/{nodeId}/move/{newParentId}")
    public ResponseEntity<LocationNode> moveNode(@PathVariable Integer nodeId, @PathVariable Integer newParentId) {
        return ResponseEntity.ok(service.moveNode(nodeId, newParentId));
    }

    // 6. Reorder nodes
    @PutMapping("/{parentId}/reorder")
    public ResponseEntity<Void> reorderNodes(@PathVariable Integer parentId, @RequestBody List<Integer> orderedNodeIds) {
        service.reorderNodes(parentId, orderedNodeIds);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<LocationNode> getNode(@PathVariable Long id) {
//        LocationNode node = locationNodeService.getNode(id);
//        return ResponseEntity.ok(node);
//    }
//
//    @GetMapping("/tree")
//    public ResponseEntity<List<LocationNode>> getTree() {
//        return ResponseEntity.ok(locationNodeService.getTree());
//    }
//
//    @PostMapping("/{parentId}")
//    public ResponseEntity<LocationNode> createNode(@PathVariable Long parentId, @RequestBody String name) {
//        LocationNode newNode = locationNodeService.createNode(parentId, name);
//        return ResponseEntity.status(HttpStatus.CREATED).body(newNode);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<LocationNode> updateNode(@PathVariable Long id, @RequestBody String newName) {
//        LocationNode updatedNode = locationNodeService.updateNode(id, newName);
//        return ResponseEntity.ok(updatedNode);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteNode(@PathVariable Long id) {
//        locationNodeService.deleteNode(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping("/move/{nodeId}/{newParentId}")
//    public ResponseEntity<Void> moveNode(@PathVariable Long nodeId, @PathVariable Long newParentId) {
//        locationNodeService.moveNode(nodeId, newParentId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping("/reorder/{nodeId}/{newPosition}")
//    public ResponseEntity<Void> reorderNode(@PathVariable Long nodeId, @PathVariable Integer newPosition) {
//        locationNodeService.reorderNode(nodeId, newPosition);
//        return ResponseEntity.noContent().build();
//    }
}