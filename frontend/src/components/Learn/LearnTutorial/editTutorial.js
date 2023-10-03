import React, { useContext, useEffect } from "react";
import { Box, Grid } from "@mui/material";
import LearnCreate from "./LearnCreate/learnCreate";
import LearnPreview from "./LearnPreview/learnPreview";
import { GlobalContext } from "../../../context/globalContext";
import { useParams } from "react-router-dom";
import axios from "axios";
import VideoInfo from "../LearnVideo/videoInfo";
import { useMediaQuery } from "react-responsive";
import PartialStepper from "../../utils/partialStepper";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function EditTutorial() {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });
  const context = useContext(GlobalContext);
  const { activeStep, setActiveStep, setVideo, video } = context;

  const params = useParams();

  useEffect(() => {
    axios
      .get(`${BASE_URL}/video/videoInfo/${params.videoId}`)
      .then((res) => {
        setVideo(res.data);
      })
      .catch((err) => console.log(err));
    // eslint-disable-next-line
  }, [params.videoId]);

  const handleNextStep = () => {
    setActiveStep((prevActiveStep) => prevActiveStep + 1);
  };

  const handleBackStep = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  function editTutorial() {
    switch (activeStep) {
      case 0:
        return <LearnCreate />;
      case 1:
        return <LearnPreview currentStep={activeStep} />;
      default:
        break;
    }
  }

  return (
    <div style={{ minHeight: isTabletOrMobile ? "300vh" : "120vh" }}>
      <Box className="full-width-height">
        <Grid container>
          <Grid item xs={12} md={7}>
            <div className="video-ratio">
              <video
                src={video?.location}
                alt="video"
                controls
                poster={video?.thumbnail}
                style={{ width: "100%", aspectRatio: "16/9" }}
              />
            </div>
            <VideoInfo video={video} />
          </Grid>
          <Grid item xs={12} md={5} className="pt-0 plr-1">
            <div
              className="teach-info-container"
              style={{ minHeight: isTabletOrMobile && "60vh" }}
            >
              <div className="teach-info-subcontainer">{editTutorial()}</div>
              <PartialStepper
                currentStep={activeStep}
                handleBackParticularStep={handleBackStep}
                handleNextParticularStep={handleNextStep}
                edit={true}
              />
            </div>
          </Grid>
        </Grid>
      </Box>
    </div>
  );
}
