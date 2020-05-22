import React, {Component, useState, useEffect} from 'react';
import axios from 'axios';

function Renameable(props){
    let [state, setState] = useState({modifiying: false, name:props.file.fileName});

    const click = () => {
        if(state.modifiying) {
            let old_path = props.path + "/" + props.file.fileName;
            if (old_path[0] == "/"){
                old_path = old_path.slice(1);
            }

            axios.post("/api/renameFile?path="+old_path+"&newName="+state.name)
            .then(res => console.log(res))
            props.file.fileName = state.name;

        }

        setState({...state, modifiying:!state.modifiying})

    };

    return (
    <div>
        {state.modifiying ?
        <input type="text" className="comp" onChange={(e) => setState({...state, name:e.target.value})} value={state.name}/> :
        <p> {state.name} </p>}
        <button className="comp" type="button" onClick={click}>
        {state.modifiying ? "save" : "rename"} </button>
    </div>
    );
}

export default Renameable;