import React, { useState, useEffect, useContext } from "react";
import { Button, Grid, Grow, TextField } from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import ExpandLessIcon from "@mui/icons-material/ExpandLess";
import axios from "axios";
import { GrNotes } from "react-icons/gr";
import { ImPencil } from "react-icons/im";
import Snack from "../../../utils/snack";
import { GlobalContext } from "../../../../context/globalContext";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function Notes({ preview, stepId }) {
  const [showNotes, setShowNotes] = useState(false);
  const [notes, setNotes] = useState("");
  const [saved, setSaved] = useState(false);
  const [hasNotes, setHasNotes] = useState(false);

  const context = useContext(GlobalContext);
  const { config, loggedUser, video } = context;

  useEffect(() => {
    let isMounted = true;
    if (!preview && loggedUser && video !== "" && video !== undefined) {
      axios
        .get(
          `${BASE_URL}/interaction/getSavedNotes/${video.id}/${stepId}`,
          config
        )
        .then((res) => {
          if(isMounted) {
            setNotes(res.data);
            if (res.data !== "") setHasNotes(true);
          }
        })
        .catch((err) => console.log(err));
    }
    return () => { isMounted = false };
    // eslint-disable-next-line
  }, [config, preview, stepId]);

  const saveNotes = () => {
    if (!preview && loggedUser && video !== "" && video !== undefined) {
      axios
        .post(
          `${BASE_URL}/interaction/saveNotes/${video.id}/${stepId}`,
          { notes },
          config
        )
        .then((res) => setSaved(true))
        .catch((err) => console.log(err));
      if (notes !== "") setHasNotes(true);
    } else if (!loggedUser) alert("You need to login");
  };

  return (
    <>
      <Grid item xs={12} style={{ display: preview ? "none" : "block" }}>
        <div onClick={() => setShowNotes(!showNotes)} className="notesDiv">
          <GrNotes />
          <span style={{ marginTop: "1.5rem" }}>Notes</span>
          {showNotes ? <ExpandLessIcon /> : <ExpandMoreIcon />}
        </div>
        <Grow in={showNotes}>
          <Grid container style={{ display: showNotes ? "flex" : "none" }}>
            <Grid
              item
              xs={12}
              className="editNotes"
              style={{ display: hasNotes ? "block" : "none" }}
            >
              <p style={{ margin: "1rem 0" }} >
                <i> {notes} </i>
              </p>
              <div onClick={() => setHasNotes(!hasNotes)}>
                <ImPencil className="pencil-notes" />
                <p style={{ marginTop: "0.5rem" }}>
                  <small>
                    <u> Edit notes </u>{" "}
                  </small>
                </p>
              </div>
            </Grid>
            <Grid item xs={10} style={{ display: !hasNotes ? "block" : "none" }}>
              <TextField
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
                multiline
                label="Write your notes here"
                fullWidth
                variant="standard"
                style={{ marginTop: "1rem" }}
              />
            </Grid>
            <Grid item xs={2} style={{ display: !hasNotes ? "block" : "none" }}>
              <Button
                onClick={saveNotes}
                variant="outlined"
                className="btnSaveNotes"
              >
                Save
              </Button>
            </Grid>
          </Grid>
        </Grow>
      </Grid>
      <Snack message="Notes saved!" successful={saved} />
    </>
  );
}
