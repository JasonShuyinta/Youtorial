import React from "react";
import { CircularProgress } from "@mui/material";

export default function Loading() {
  return (
    <div className="loading-div">
      <h3>Loading...</h3>
      <CircularProgress style={{ color: "black" }} />
    </div>
  );
}
