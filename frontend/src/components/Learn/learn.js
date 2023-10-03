import React, { useEffect, useContext, useState } from "react";
import { Grid } from "@mui/material";
import LearnVideo from "./LearnVideo/learnVideo";
import { GlobalContext } from "../../context/globalContext";
import LearnTutorial from "./LearnTutorial/learnTutorial";
import Comments from "./LearnComments/comments";
import axios from "axios";
import VideoInfo from "./LearnVideo/videoInfo";
import { useMediaQuery } from "react-responsive";
import Linkify from "react-linkify";
import parse from "html-react-parser"

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function Learn() {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });
  
  const [videoAuthor, setVideoAuthor] = useState("");

  const context = useContext(GlobalContext);
  const { video, setVideo } = context;

  const videoId = window.location.href.split("learn/")[1];

  useEffect(() => {
    axios
      .get(`${BASE_URL}/video/videoInfo/${videoId}`)
      .then((res) => {
        setVideo(res.data);
        axios
          .get(`${BASE_URL}/user/${res.data.author}`)
          .then((resUser) => setVideoAuthor(resUser.data))
          .catch((err) => console.log(err));
      })
      .catch((err) => console.log(err));

    //eslint-disable-next-line
  }, []);

  return (
    <>
      {isTabletOrMobile ? (
        <div className="min-height-container">
          <div className="learnvideo-mobile">
            <LearnVideo />
          </div>
          <div className="learntutorial-mobile">
            <LearnTutorial videoAuthor={videoAuthor} />
          </div>
          <div>
            <Grid container>
              <Grid item xs={12}>
                <VideoInfo video={video} />
              </Grid>
              <Grid item xs={12}>
                <Comments />
              </Grid>
            </Grid>
          </div>
        </div>
      ) : (
        <div className="learn-page">
          <div className="learn-container">
            <Grid container>
              <Grid item xs={7} style={{ padding: "0 1rem 1rem 1rem" }}>
                <div>
                  <LearnVideo />
                </div>
                <VideoInfo video={video} />
              </Grid>
              <Grid item xs={5} style={{padding: "0 1rem"}}>
                <LearnTutorial videoAuthor={videoAuthor} />
              </Grid>
            </Grid>
          </div>
          <div className="learn-container-secondary">
            <div className="video-description-secondary">
              {video?.description === "" ? (
                <p>No video description</p>
              ) : (
                <Linkify>{video.description ? parse(video.description) : ""}</Linkify>
              )}
            </div>
            <Comments />
          </div>
        </div>
      )}
    </>
  );
}
