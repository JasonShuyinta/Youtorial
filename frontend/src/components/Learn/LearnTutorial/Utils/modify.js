import React, { useContext } from "react";
import { Grid } from "@mui/material";
import { ImPencil } from "react-icons/im";
import { FaTrashAlt } from "react-icons/fa";
import { CreationContext } from "../../../../context/creationContext";

export default function Modify({ preview, handleDeleteStep, stepId }) {
  const create = useContext(CreationContext);
  const { handleModify } = create;

  return (
    <Grid item xs={12} style={{ display: preview ? "block" : "none" }}>
      <div className="disp-flex">
        <div className="modAndDel">
          {" "}
          <ImPencil />{" "}
        </div>
        <p className="modifyAndDelete" onClick={() => handleModify(stepId)}>
          <small>
            <u>Modify</u>
          </small>
        </p>
        &nbsp; &nbsp;
        <div className="modAndDel">
          {" "}
          <FaTrashAlt />{" "}
        </div>
        <p className="modifyAndDelete" onClick={() => handleDeleteStep(stepId)}>
          <u>
            <small>Delete</small>
          </u>
        </p>
      </div>
    </Grid>
  );
}
