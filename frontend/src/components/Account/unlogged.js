import React from "react";
import {  Typography } from "@mui/material";
import { Link } from "react-router-dom";

export default function Unlogged() {
  return (
    <>
      <div className="notlogged-stats">
        <Typography variant="subtitle1">
          You need to{" "}
          <span className="link">
            <Link to="/account/login">login</Link>
          </span>{" "}
          or{" "}
          <span className="link">
            <Link to="/account/signup">signup</Link>
          </span>{" "}
          to view this section!
        </Typography>
      </div>
    </>
  );
}
