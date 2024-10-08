import React from 'react';
import { Draggable, Droppable } from 'react-beautiful-dnd';

const Node = ({
    node,
    level,
    localIndex,
    nodes,
    expandedNodes,
    toggleExpand,
    setAddParentId,
    setEditNodeId,
    setCloseEditNodeId,
    editNodeId,
    closeEditNodeId,
    deleteNode
}) => {
    const childNodes = nodes.filter(n => n.parentNodeId === node.id).sort((a, b) => a.ordering - b.ordering);
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
                                        <Node
                                            key={childNode.id}
                                            node={childNode}
                                            level={level + 1}
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
                            </td>
                        </tr>
                    )}
                </Droppable>
            )}
        </React.Fragment>
    );
};

export default Node;