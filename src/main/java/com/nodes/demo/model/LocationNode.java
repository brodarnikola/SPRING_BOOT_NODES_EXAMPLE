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

//    @ManyToOne
//    @JoinColumn(name = "parent_node_id", referencedColumnName = "id")
//    @JsonBackReference
//    private LocationNode parentNode;

    @Column(name = "parent_node_id")
    private Integer parentNodeId;

    @Column
    private Integer ordering;
//
//    @OneToMany(mappedBy = "parentNode")
//    @JsonManagedReference  // Serializes child nodes
//    private List<LocationNode> children;
//
//
//
//    public List<LocationNode> getChildren() {
//        return children;
//    }
//
//    public void setChildren(List<LocationNode> children) {
//        this.children = children;
//    }


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

    public Integer getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(Integer parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public Integer getOrdering() {
        return ordering;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

}