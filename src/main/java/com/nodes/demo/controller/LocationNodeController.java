package com.nodes.demo.controller;

import com.nodes.demo.model.LocationNode;
import com.nodes.demo.service.LocationNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/nodes")
public class LocationNodeController {

//    @Autowired
    private LocationNodeService locationNodeService;

    @Autowired
    private LocationNodeService service;

    // 1. Get the tree or specific node
    @GetMapping("/{parentId}")
    public ResponseEntity<List<LocationNode>> getTree(@PathVariable Integer parentId) {
        return ResponseEntity.ok(service.getTree(parentId));
    }

    // 2. Add a new node
    @PostMapping("/{parentId}")
    public ResponseEntity<LocationNode> addNode(@PathVariable Integer parentId, @RequestParam String title) {
        return ResponseEntity.ok(service.addNode(parentId, title));
    }

    // 3. Update node
    @PutMapping("/{nodeId}")
    public ResponseEntity<LocationNode> updateNode(@PathVariable Integer nodeId, @RequestParam String title) {
        return ResponseEntity.ok(service.updateNode(nodeId, title));
    }

    // 4. Delete node
    @DeleteMapping("/{nodeId}")
    public ResponseEntity<Void> deleteNode(@PathVariable Integer nodeId) {
        service.deleteNode(nodeId);
        return ResponseEntity.ok().build();
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

}