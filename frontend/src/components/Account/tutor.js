import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import { Avatar, Typography, Rating, Grid, Paper } from "@mui/material";
import { GlobalContext } from "../../context/globalContext";
import { useMediaQuery } from "react-responsive";
import { timeConverter } from "../utils/helper"

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function Tutor() {
  const [videos, setVideos] = useState([]);

  const context = React.useContext(GlobalContext);
  const { tutorSub } = context;

  useEffect(() => {
    if (tutorSub !== null) {
      axios
        .get(`${BASE_URL}/video/videoByUser/${tutorSub?.id}`)
        .then((res) => {
          setVideos(res.data);
        })
        .catch((err) => console.log(err));
    }
  }, [tutorSub]);

  return (
    <div className="min-height-container">
      <div className="tutor-info-container">
        <Avatar src={tutorSub?.image} alt="avatar" className="tutor-avatar" />
        <Typography variant="h5" style={{ marginRight: "2rem" }}>
          {tutorSub?.username}
        </Typography>
        <Rating name="read-only" value={4} readOnly size="large" />
      </div>
      <div>
        <Grid container>
          {videos &&
            videos.length > 0 &&
            videos.map((video) => {
              return <Video key={video.id} video={video} />;
            })}
        </Grid>
      </div>
    </div>
  );
}

const Video = ({ video }) => {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });
  const [duration, setDuration] = useState(null);

  useEffect(() => {
    if (video?.duration) {
      setDuration(timeConverter(video.duration));
    }
  }, [video]);

  return (
    <>
      <Grid item xs={12} sm={4} md={3} className="tutor-container">
        <Link to={{ pathname: `/learn/${video.id}` }}>
          <Paper elevation={3} className="hvr-underline-from-center ">
            <div
              className={
                isTabletOrMobile ? "img-container-mobile" : "img-container"
              }
            >
              <img src={video.thumbnail} alt="thumbnail" />
            </div>
            <div className="info-container">
              <Typography variant="h6" className="video-title">
                {video.title}
              </Typography>
              <Typography variant="subtitle1">{duration}</Typography>
              <div className="category-container">
                {video?.category?.length !== 0 &&
                  video?.category.map((cat) => {
                    return <span key={cat}>#{cat} &nbsp;</span>;
                  })}
              </div>
            </div>
          </Paper>
        </Link>
      </Grid>
    </>
  );
};
