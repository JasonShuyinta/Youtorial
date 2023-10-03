import React, { useEffect, useState } from "react";
import axios from "axios";
import { Grid, Typography } from "@mui/material";
import Video from "./video";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function Tutorials({ config }) {
  const [tutorials, setTutorials] = useState([]);

  useEffect(() => {
    axios
      .get(`${BASE_URL}/public/getUserTutorials`, config)
      .then((res) => setTutorials(res.data))
      .catch((e) => console.log(e));
  }, [config]);

  return (
    <>
      {tutorials.length > 0 ? (
        <>
          <Typography variant="h5" className="tutorial-text">
            Tutorials you have created
          </Typography>
          <Grid container className="tutorial-text-container">
            {tutorials.map((tutorial, i) => {
              return <VideoRender tutorial={tutorial} key={i} />;
            })}
          </Grid>
        </>
      ) : (
        <div className="center tutorial-text-empty">
          <Typography variant="h5">You haven't created any tutorial</Typography>
        </div>
      )}
    </>
  );
}

function VideoRender({ tutorial }) {
  const [video, setVideo] = useState(null);

  useEffect(() => {
    axios
      .get(`${BASE_URL}/video/videoInfo/${tutorial?.steps[0].videoId}`)
      .then((res) => setVideo(res.data))
      .catch((e) => console.log(e));
  }, [tutorial]);

  return <Video video={video} />;
}
