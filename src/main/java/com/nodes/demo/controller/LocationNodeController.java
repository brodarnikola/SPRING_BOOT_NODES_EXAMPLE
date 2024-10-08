package com.nodes.demo.controller;

import com.nodes.demo.model.LocationNode;
import com.nodes.demo.model.request.ReorderRequest;
import com.nodes.demo.service.LocationNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nodes")
public class LocationNodeController {

    @Autowired
    private LocationNodeService service;

    @GetMapping("/tree")
    public ResponseEntity<List<LocationNode>> getTree() {
        List<LocationNode> listNodes = service.getTree();

        return ResponseEntity.ok(listNodes);
    }

    @PostMapping("/addParent/{title}")
    public ResponseEntity<LocationNode> addParentNode( @PathVariable String title) {

        try {
            return ResponseEntity.ok(service.addParentNode( title));
        } catch (Exception e) {
            return ResponseEntity.ok(service.addParentNode( title));
        }
    }

    @PostMapping("/addChild/{parentId}/{title}")
    public ResponseEntity<LocationNode> addChildNode( @PathVariable Integer parentId, @PathVariable String title) {

        try {
            return ResponseEntity.ok(service.addChildNode(parentId, title));
        } catch (Exception e) {
            return ResponseEntity.ok(service.addChildNode(parentId, title));
        }
    }

    @PutMapping("/update/{nodeId}/{title}")
    public ResponseEntity<LocationNode> updateNode(@PathVariable Integer nodeId, @PathVariable String title) {
        return ResponseEntity.ok(service.updateNode(nodeId, title));
    }

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

    @PostMapping("/reorder")
    public ResponseEntity<Void> reorderNodes(@RequestBody List<ReorderRequest> reorderRequests) {
        service.reorderNodes(reorderRequests);
        return ResponseEntity.ok().build();
    }

}