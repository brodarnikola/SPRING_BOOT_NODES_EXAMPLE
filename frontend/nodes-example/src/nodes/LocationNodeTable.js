import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { DragDropContext, Droppable, Draggable } from 'react-beautiful-dnd';
import { API_BASE_URL } from '../constants';
import './locationsTableCss.css';

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

        console.log("destionation parent id: ", destinationParentId);
        console.log("destination.index: ", destination.index);

        try {
            await axios.post(`${API_BASE_URL}/reorder`, affectedNodes);
        } catch (error) {
            console.error("Error updating nodes", error);
            // Revert changes if the update fails
            fetchNodes();
        }

        // try {
        //     await axios.post(`${API_BASE_URL}/reorder`, {
        //         parentNodeId: destinationParentId,
        //         ordering: destination.index
        //     });
        //     fetchNodes();
        //     setParentTitle('');
        // } catch (error) {
        //     console.error("Error adding parent node", error);
        // }

        // // Call backend to update the nodes
        // try {
        //     await Promise.all(affectedNodes.map(node => 
        //         axios.put(`${API_BASE_URL}/nodes/${node.id}`, {
        //             parentNodeId: node.parentNodeId,
        //             ordering: node.ordering
        //         })
        //     ));
        // } catch (error) {
        //     console.error("Error updating nodes", error);
        //     // Revert changes if the update fails
        //     fetchNodes();
        // }
    };

    const renderNode = (node, level = 0, localIndex = 0) => {
        const childNodes = 
            nodes
            .filter(n => n.parentNodeId === node.id)
            .sort((a, b) => a.ordering - b.ordering);
        const isExpanded = expandedNodes[node.id];

        return (
            <React.Fragment key={node.id}>
                <Draggable draggableId={node.id.toString()} index={localIndex}>
                    {(provided) => (
                        <tr
                            ref={provided.innerRef}
                            {...provided.draggableProps}
                            {...provided.dragHandleProps}
                            className={`level-${level}`}
                        >
                            <td>{node.id}</td>
                            <td style={{ paddingLeft: `${level * 20}px` }}>{node.title}</td>
                            <td>{node.ordering}</td>
                            <td>
                                {node.parentNodeId === null && 
                                    <button onClick={() => setAddParentId(node.id)}>Add</button>
                                }
                                <button onClick={() => {
                                    if (node.id === editNodeId && !closeEditNodeId) {
                                        setCloseEditNodeId(true)
                                    } else {
                                        setCloseEditNodeId(false)
                                    }
                                    setEditNodeId(node.id)
                                }}>Edit</button>
                                {
                                    node.id != 1 &&    
                                    <button onClick={() => deleteNode(node.id)}>Delete</button>
                                }
                                {childNodes.length > 0 && (
                                    <button onClick={() => toggleExpand(node.id)}>
                                        {isExpanded ? 'Collapse' : 'Expand'}
                                    </button>
                                )}
                            </td>
                        </tr>
                    )}
                </Draggable>
                {isExpanded && (
                    <Droppable droppableId={node.id.toString()} type="NODE">
                        {(provided) => (
                            <tr>
                                <td colSpan="4">
                                    <table {...provided.droppableProps} ref={provided.innerRef}>
                                        <tbody> 
                                        {childNodes.map((childNode, index) => (
                                            renderNode(childNode, level + 1, index)
                                        ))}
                                        {provided.placeholder} 
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                        )}
                    </Droppable>
                )}
            </React.Fragment>
        );
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
                // , { title: parentTitle });
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
                                {nodes.filter(node => node.parentNodeId === null).map((node, index) => renderNode(node, 0, index))}
                                {provided.placeholder}
                            </tbody>
                        </table>
                    )}
                </Droppable> 

                
                {addParentId && (
                <div>
                    <h2>Add Child Node under Parent ID {addParentId}</h2>
                    <input
                        type="text"
                        value={childTitle}
                        onChange={(e) => setChildTitle(e.target.value)}
                        placeholder="Child node title"
                    />
                    <button onClick={() => addNode(addParentId, childTitle)}>Add Child</button>
                </div>
            )}

            {editNodeId && !closeEditNodeId && (
                <div>
                    <h2>Edit Node {editNodeId}</h2>
                    <input
                        type="text"
                        value={parentTitle}
                        onChange={(e) => setParentTitle(e.target.value)}
                        placeholder="New title"
                    />
                    <button onClick={editNode}>Save</button>
                </div>
            )}

            <div>
                <h2>Add new parent node</h2>
                <input
                    type="text"
                    value={parentTitle}
                    onChange={(e) => setParentTitle(e.target.value)}
                    placeholder="Parent node title"
                />
                <button onClick={() => addParentNode(parentTitle)}>Add parent</button>
            </div>
            </div>
        </DragDropContext>
    );
};

export default LocationNodeTable;