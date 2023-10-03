import React, { useContext } from "react";
import { Grid, TextField } from "@mui/material";
import { CreationContext } from "../../../../../context/creationContext";

export default function Time() {
  const create = useContext(CreationContext);
  const {
    startTime,
    endTime,
    setEndTime,
    timeError,
    setTimeError,
    overTimeError,
    setOverTimeError,
  } = create;

  return (
    <>
      <Grid container>
        <Grid item xs={6} className="center-and-start">
          <div className="center-and-start">
            <div>
              <p>Starts at:</p>
              <p>{startTime}</p>
            </div>
          </div>
        </Grid>
        <Grid item xs={6} className="center-and-end">
          <div className="center-and-end">
            <div>
              <div className="center-and-end">
                <p>Ends at:</p>
              </div>
              <TextField
                type="time"
                inputProps={{ step: 1 }}
                variant="outlined"
                value={endTime}
                onChange={(e) => {
                  setEndTime(e.target.value);
                  setTimeError("");
                  setOverTimeError("");
                }}
              />
            </div>
          </div>
          <br></br>
        </Grid>
        <Grid
          item
          xs={12}
          className="center-and-end p-1"
          style={{ display: !timeError && !overTimeError ? "none" : "flex" }}
        >
          <div>
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
              <small>End time must be inside the video length</small>
            </p>
          </div>
        </Grid>
      </Grid>
    </>
  );
}
