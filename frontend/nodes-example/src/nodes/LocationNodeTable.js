import React, { useState, useEffect } from 'react'; 
import { DragDropContext, Droppable } from 'react-beautiful-dnd'; 
import axios from 'axios';
import { API_BASE_URL } from '../constants';

import './locationsTableCss.css';
import Node from './Node';
import NodeActions from './NodeActions'; 

const LocationNodeTable = () => {
    const [nodes, setNodes] = useState([]);
    const [expandedNodes, setExpandedNodes] = useState({});
    const [parentTitle, setParentTitle] = useState('');
    const [editNodeId, setEditNodeId] = useState(null);
    const [closeEditNodeId, setCloseEditNodeId] = useState(false);
    const [childTitle, setChildTitle] = useState('');
    const [addParentId, setAddParentId] = useState(null);

    useEffect(() => {
        fetchNodes();
    }, []);

    const fetchNodes = async () => {
        try {
            const response = await axios.get(`${API_BASE_URL}/tree`);
            setNodes(response.data);
        } catch (error) {
            console.error("Error fetching nodes", error);
        }
    };

    const toggleExpand = (nodeId) => {
        setExpandedNodes(prev => ({
            ...prev,
            [nodeId]: !prev[nodeId]
        }));
    };

    const onDragEnd = async (result) => {
        const { destination, source, draggableId } = result;

        if (!destination) {
            return;
        }

        if (
            destination.droppableId === source.droppableId &&
            destination.index === source.index
        ) {
            return;
        }

        const sourceParentId = parseInt(source.droppableId);
        const destinationParentId = parseInt(destination.droppableId);

        const updatedNodes = Array.from(nodes);
       
        const [reorderedNode] = updatedNodes.splice(updatedNodes.findIndex(node => node.id.toString() === draggableId), 1); 

        reorderedNode.parentNodeId = destinationParentId; 
 
         // Insert the node at the new position
         updatedNodes.splice(
            updatedNodes.findIndex(node => node.parentNodeId === destinationParentId) + destination.index, 
            0,
            reorderedNode
        ) ;
 
        // Update ordering for affected nodes
        const affectedNodes = updatedNodes.filter(node => 
            node.parentNodeId === sourceParentId || node.parentNodeId === destinationParentId
        );

        affectedNodes.forEach((node, index) => {
            node.ordering = index; 
        }); 
 
        setNodes(updatedNodes);

        try { 
            await axios.post(`${API_BASE_URL}/reorder`, affectedNodes); 
            // await reorderNodes(affectedNodes);
        } catch (error) {
            console.error("Error updating nodes", error); 
            fetchNodes(setNodes);
        } 
    };

    const addParentNode = async (title) => {
        try {
            await axios.post(`${API_BASE_URL}/addParent/${title}`);
            fetchNodes();
            setParentTitle('');
        } catch (error) {
            console.error("Error adding parent node", error);
        }
    };

    const addNode = async (parentId, title) => {
        try {
            await axios.post(`${API_BASE_URL}/addChild/${parentId}/${title}`);
            fetchNodes();
            setChildTitle('');
            setAddParentId(null);
        } catch (error) {
            console.error("Error adding child node", error);
        }
    };

    const editNode = async () => {
        try {
            await axios.put(`${API_BASE_URL}/update/${editNodeId}/${parentTitle}`); 
            fetchNodes();
            setEditNodeId(null);
            setParentTitle('');
        } catch (error) {
            console.error("Error editing node", error);
        }
    };

    const deleteNode = async (nodeId) => {
        try {
            await axios.delete(`${API_BASE_URL}/${nodeId}`);
            fetchNodes();
        } catch (error) {
            console.error("Error deleting node", error);
        }
    };


    return (
        <DragDropContext onDragEnd={onDragEnd}>
            <div className="location-node-table">
                <h1>Location Node Table</h1>

                <Droppable droppableId="root" type="NODE">
                    {(provided) => (
                        <table {...provided.droppableProps} ref={provided.innerRef}>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Title</th>
                                    <th>Ordering</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {nodes.filter(node => node.parentNodeId === null).map((node, index) => (
                                    <Node
                                        key={node.id}
                                        node={node}
                                        level={0}
                                        localIndex={index}
                                        nodes={nodes}
                                        expandedNodes={expandedNodes}
                                        toggleExpand={toggleExpand}
                                        setAddParentId={setAddParentId}
                                        setEditNodeId={setEditNodeId}
                                        setCloseEditNodeId={setCloseEditNodeId}
                                        editNodeId={editNodeId}
                                        closeEditNodeId={closeEditNodeId}
                                       deleteNode={deleteNode}
                                    />
                                ))}
                                {provided.placeholder}
                            </tbody>
                        </table>
                    )}
                </Droppable> 

                <NodeActions
                    addParentId={addParentId}
                    setAddParentId={setAddParentId}
                    childTitle={childTitle}
                    setChildTitle={setChildTitle}
                    parentTitle={parentTitle}
                    setParentTitle={setParentTitle}
                    editNodeId={editNodeId}
                    closeEditNodeId={closeEditNodeId}
                    addNode={addNode}
                    editNode={editNode}
                    addParentNode={addParentNode}
                />
            </div>
        </DragDropContext>
    );
};

export default LocationNodeTable;