import React, { useState, useEffect, useContext } from "react";
import axios from "axios";
import { Grid, Typography, Skeleton } from "@mui/material";
import Video from "./video";
import { GlobalContext } from "../../context/globalContext";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function VideoUploaded() {
  const [videos, setVideos] = useState([]);
  const [loading, setLoading] = useState(true);

  const context = useContext(GlobalContext);
  const { loggedUser, successfulUpdate } = context;

  useEffect(() => {
    if (loggedUser) {
      axios
        .get(`${BASE_URL}/video/videoByUser/${loggedUser.id}`)
        .then((res) => {
          setVideos(res.data);
          setLoading(false);
        })
        .catch((error) => console.log(error));
    }
  }, [loggedUser, successfulUpdate]);

  function Skel() {
    var skeletonNum = [];
    for (let i = 0; i < 20; i++) {
      skeletonNum.push(
        <Grid item xs={12} sm={6} md={3} className="feed-video-grid" key={i}>
          <Skeleton variant="rect" style={{height: "40vh"}} />
        </Grid>
      );
    }
    return <>{skeletonNum}</>;
  }

  return (
    <div>
      {loading ? (
        <div className="p-1">
          <Skeleton
            variant="rect"
            style={{ width: "40%", height: "50px", padding: "0 1rem", marginLeft: "1rem" }}
          />
          <div className="feed-video-container">
            <Grid container spacing={1}>
              <Skel />
            </Grid>
          </div>
        </div>
      ) : (
        <>
          {videos.length !== 0 ? (
            <>
              <div style={{ padding: "0 1rem" }}>
                <Typography variant="h4" className="p-1">
                  Uploaded videos
                </Typography>
              </div>
              <Grid container className="p-1">
                {videos.map((video) => {
                  return <Video video={video} key={video.id} />;
                })}
              </Grid>
            </>
          ) : (
            <div className="center mt-4 min-height-container">
              <Typography variant="h5">
                You haven't uploaded any video!
              </Typography>
            </div>
          )}
        </>
      )}
    </div>
  );
}
