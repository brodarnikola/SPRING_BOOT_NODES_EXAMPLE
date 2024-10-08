import React from 'react';

const NodeActions = ({
    addParentId,
    setAddParentId,
    childTitle,
    setChildTitle,
    parentTitle,
    setParentTitle,
    editNodeId,
    closeEditNodeId,
    addNode,
    editNode,
    addParentNode
}) => { 
  
    return (
        <div>
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

export default NodeActions;