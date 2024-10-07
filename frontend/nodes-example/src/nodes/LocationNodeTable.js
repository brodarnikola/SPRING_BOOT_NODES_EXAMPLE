import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { API_BASE_URL } from '../constants'; 

const LocationNodeTable = () => {
    const [nodes, setNodes] = useState([]);
    const [parentTitle, setParentTitle] = useState('');
    const [parentId, setParentId] = useState(1);  // Default parentId for root node
    const [editNodeId, setEditNodeId] = useState(null);
    const [childTitle, setChildTitle] = useState('');
    const [addParentId, setAddParentId] = useState(null);  // Parent ID for adding child

    // Fetch tree structure on component mount
    useEffect(() => {
        fetchNodes(parentId); // Fetch root and children initially
    }, []);

    const fetchNodes = async (parentId) => {
        try { 
            const response = await axios.get(`${API_BASE_URL}/tree`);  // await axios.get(`${API_BASE_URL}/${parentId}`);
            // const response = await axios.get(`/api/locationNodes/${parentId}`);
            setNodes(response.data);
        } catch (error) {
            console.error("Error fetching nodes", error);
        }
    };
 
    // Add new node
    const addParentNode = async (title) => {
        try {
            // await axios.post(`${API_BASE_URL}/${parentId}`, { title });
            // await axios.post(`${API_BASE_URL}/`, { title });
            await axios.post(`${API_BASE_URL}/addParent/${title}`);
            fetchNodes(parentId);  // Refresh the tree
            setParentTitle('');  // Clear input field
        } catch (error) {
            console.error("Error adding parent node", error);
        }
    };

    // Add new node
    const addNode = async (parentId, title) => {
        try {
            await axios.post(`${API_BASE_URL}/addChild/${parentId}/${title}`);
            // await axios.post(`/api/locationNodes/${parentId}`, { title });
            fetchNodes(parentId);  // Refresh the tree
            setChildTitle('');  // Clear input field
            setAddParentId(null); // Reset parent ID for adding
        } catch (error) {
            console.error("Error adding child node", error);
        }
    };

    // Edit node
    const editNode = async () => {
        try {
            await axios.put(`${API_BASE_URL}/${editNodeId}`, { title: parentTitle });
            fetchNodes(parentId);
            setEditNodeId(null);  // Clear edit mode
            setParentTitle('');
        } catch (error) {
            console.error("Error editing node", error);
        }
    };

    // Delete node
    const deleteNode = async (nodeId) => {
        try {
            await axios.delete(`${API_BASE_URL}/${nodeId}`);
            fetchNodes(parentId);
        } catch (error) {
            console.error("Error deleting node", error);
        }
    };

    return (
        <div>
            <h1>Location Node Table</h1>

            {/* Table to display nodes */}
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Ordering</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {nodes.map((node) => (
                        <tr key={node.id}>
                            <td>{node.id}</td>
                            <td>{node.title}</td>
                            <td>{node.ordering}</td>
                            <td>
                                <button onClick={() => setAddParentId(node.id)}>Add</button>
                                <button onClick={() => setEditNodeId(node.id)}>Edit</button>
                                <button onClick={() => deleteNode(node.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* Add child node */}
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

            {/* Edit node */}
            {editNodeId && (
                <div>
                    <h2>Edit Node</h2>
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
                    <h2>Add new parent node </h2>
                    <input
                        type="text"
                        value={parentTitle}
                        onChange={(e) => setParentTitle(e.target.value)}
                        placeholder="Parent node title"
                    />
                    <button onClick={() => addParentNode(parentTitle)}>Add parent</button>
                </div>
        </div>
    );
};

export default LocationNodeTable;
            