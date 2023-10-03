import React, { useContext } from "react";
import { TextField, Grid } from "@mui/material";
import { CreationContext } from "../../../../../context/creationContext";

export default function TimeFullView({ video }) {
  const create = useContext(CreationContext);
  const { startTime, endTime, setEndTime, timeError, overTimeError } = create;

  return (
    <Grid container style={{ marginTop: "1rem" }}>
      <Grid item xs={6}>
        <p style={{ marginTop: "1rem" }}> Start time: {startTime}</p>
      </Grid>
      <Grid item xs={6}>
        <>
          <div className="disp-flex">
            <p className="mr-1">Ends at </p>
            <TextField
              type="time"
              inputProps={{ step: 300 }}
              variant="outlined"
              value={endTime}
              onChange={(e) => setEndTime(e.target.value)}
            />
          </div>
          <p
            className="error-msg"
            style={{ display: timeError ? "block" : "none" }}
          >
            <small>Start time can't be greater or equal than end time</small>
          </p>
          <p
            className="error-msg"
            style={{ display: overTimeError ? "block" : "none" }}
          >
            <small>Timestamps must be inside the video length</small>
          </p>
        </>
      </Grid>
    </Grid>
  );
}
