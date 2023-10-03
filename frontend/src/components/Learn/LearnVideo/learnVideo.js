import React, { useEffect, useState, useContext } from "react";
import { CircularProgress } from "@mui/material";
import { GlobalContext } from "../../../context/globalContext";

export default function LearnVideo() {
  const [loading, setLoading] = useState(true);
  const [videoFile, setVideoFile] = useState(null);

  const context = useContext(GlobalContext);
  const { video } = context;

  useEffect(() => {
    if (video) {
      setLoading(false);
      setVideoFile(video.location);
    }
  }, [video]);

  return (
    <div className="full-width-height">
      {loading ? (
        <div className="loading-spinner full-width-height center-and-align">
          <CircularProgress />
        </div>
      ) : (
        <div className="player-wrapper full-width-height">
          <video
            src={videoFile}
            controls
            poster={video?.thumbnail}
            playsInline
            id="sfx90-345"
            style={{ width: "100%", height: "100%", maxHeight: "90vh" }}
          ></video>
        </div>
      )}
    </div>
  );
}
