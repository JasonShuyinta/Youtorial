import React, { useContext } from "react";
import { Grid, Typography, Divider, Avatar } from "@mui/material";
import parse from "html-react-parser"
import Linkify from "react-linkify";
import { GlobalContext } from "../../context/globalContext";
import { useMediaQuery } from "react-responsive";

export default function UploadVideoInfo({ recurrentProps }) {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });

  const { title, description, chosenCategories } = recurrentProps;

  const { loggedUser } = useContext(GlobalContext);

  return (
    <>
      <Grid container>
        <Grid item xs={12} style={{ padding: "0.5rem 0" }}>
          <Typography variant="subtitle1" className="teach-info-title">
            {title}
          </Typography>
        </Grid>
        <Grid item xs={12} className="center-and-align">
          {chosenCategories.length > 0 &&
            chosenCategories.map((cat) => {
              return (
                <p className="teach-category" key={cat}>
                  #{cat}
                </p>
              );
            })}
          <div style={{ flexGrow: "1" }} />
        </Grid>
        <Grid item xs={3} className="center-and-align">
          <div style={{ flexGrow: "1" }} />
        </Grid>
        <Grid item xs={5}></Grid>
        <Grid
          item
          xs={12}
          className="center-and-align"
          style={{
            padding: "1vw 0",
            marginTop: isTabletOrMobile ? "1rem" : "0",
          }}
        >
          <Avatar src={loggedUser?.image} alt="Avatar" />
          <Typography
            variant="subtitle1"
            style={{ marginLeft: isTabletOrMobile ? "2vw" : "1vw" }}
          >
            {loggedUser?.username}
          </Typography>
          <div style={{ flexGrow: "1" }} />
        </Grid>
        <Grid item xs={12}>
          <Divider
            style={{
              margin: isTabletOrMobile ? "1rem 0.5rem" : "1rem 0 0 -1rem",
            }}
          />
          <div className="video-description">
            <Linkify>{parse(description)}</Linkify>
          </div>
        </Grid>
      </Grid>
    </>
  );
}
