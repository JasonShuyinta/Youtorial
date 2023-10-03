import React, { useContext } from "react";
import { Grid, Typography, Divider, Avatar } from "@mui/material";
import { FaLock, FaLockOpen } from "react-icons/fa";
import parse from "html-react-parser"
import Linkify from "react-linkify";
import { GlobalContext } from "../../context/globalContext";

export default function UploadVideoInfoMobile({ recurrentProps }) {
  const { title, description, chosenCategories, lock } = recurrentProps;

  const { loggedUser } = useContext(GlobalContext);

  return (
    <div style={{ padding: "1rem" }}>
      <h2>{title}</h2>
      <Grid container>
        <Grid item xs={12} style={{ marginTop: "1rem" }}>
          <div className="watchVideoInfo2">
            <Avatar src={loggedUser.image} alt="Avatar" />
            <Typography variant="subtitle1">
              By {loggedUser.username}{" "}
            </Typography>
          </div>
          <div>
            <Grid container className="video-mobile-publish-info">
              <Grid item xs={6}>
                Published 1 minute ago
              </Grid>
              <Grid item xs={6}>
                <span>{lock ? <FaLock /> : <FaLockOpen />}</span>
              </Grid>
            </Grid>
          </div>

          <div className="watchVideoInfo3">
            {chosenCategories.length > 0 &&
              chosenCategories.map((cat) => {
                return <p key={cat}>#{cat} &nbsp; </p>;
              })}
          </div>
        </Grid>
      </Grid>
      <Divider style={{ margin: "1rem 0 0 -1rem" }} />
      <div className="video-description">
        <Linkify>{parse(description)}</Linkify>
      </div>
    </div>
  );
}
