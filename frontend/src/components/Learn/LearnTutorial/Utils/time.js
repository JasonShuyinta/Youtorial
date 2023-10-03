import React from "react";
import { Grid } from "@mui/material";
import PlayCircleOutlineIcon from "@mui/icons-material/PlayCircleOutline";

export default function Time({ startTime, endTime }) {
  const handleSeek = () => {
    var time = startTime.split(":");
    var seekTimeInSeconds = 0;
    if (time.length > 2) {
      seekTimeInSeconds = +time[0] * 60 * 60 + +time[1] * 60 + +time[2];
    } else {
      seekTimeInSeconds = +time[0] * 60 + +time[1];
    }

    document.getElementsByTagName("video")[0].currentTime = seekTimeInSeconds;
  };

  return (
    <Grid item xs={12} style={{ margin: "0" }}>
      <div style={{ display: "flex" }}>
        <PlayCircleOutlineIcon
          onClick={() => handleSeek()}
          style={{
            marginTop: "-0.3rem",
            marginRight: "1rem",
            cursor: "pointer",
          }}
        />
        <p style={{ margin: "0" }}>
          {startTime} &nbsp; - &nbsp; {endTime}
        </p>
      </div>
    </Grid>
  );
}
