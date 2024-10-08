 

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { API_BASE_URL } from '../constants';
import './locationsTableCss.css';

const LocationNodeTableCopy = () => {
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

    const renderNode = (node, isParent = false, level = 0) => {
        const childNodes = nodes.filter(n => n.parentNodeId === node.id);
        const isExpanded = expandedNodes[node.id];

        return (
            <React.Fragment key={node.id}>
                <tr className={`level-${level}`}>
                    <td>{node.id}</td>
                    <td style={{ paddingLeft: `${level * 20}px` }}>{node.title}</td>
                    <td>{node.ordering}</td>
                    <td>
                        { isParent && 
                            ( <button onClick={() => setAddParentId(node.id)}>Add</button>)
                        }
                        <button onClick={() =>
                            {
                                if( node.id === editNodeId && !closeEditNodeId ) {
                                    setCloseEditNodeId(true)
                                }    
                                else {
                                    setCloseEditNodeId(false)
                                }
                                setEditNodeId(node.id)
                            }

                        }>Edit</button>
                        <button onClick={() => deleteNode(node.id)}>Delete</button>
                        {childNodes.length > 0 && (
                            <button onClick={() => toggleExpand(node.id)}>
                                {isExpanded ? 'Collapse' : 'Expand'}
                            </button>
                        )}
                    </td>
                </tr>
                {isExpanded && childNodes.map(childNode => renderNode(childNode, false, level + 1))}
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
            await axios.put(`${API_BASE_URL}/${editNodeId}`, { title: parentTitle });
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
        <div className="location-node-table">
            <h1>Location Node Table</h1>

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
                    {nodes.filter(node => node.parentNodeId === null).map(node => renderNode(node, true))}
                </tbody>
            </table>

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
    );
};

export default LocationNodeTableCopy;