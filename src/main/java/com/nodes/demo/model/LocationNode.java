package com.nodes.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "location_node")
public class LocationNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "parent_node_id", referencedColumnName = "id")
    private LocationNode parentNode;

    @Column(nullable = true)
    private Integer ordering;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocationNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(LocationNode parentNode) {
        this.parentNode = parentNode;
    }

    public Integer getOrdering() {
        return ordering;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

}