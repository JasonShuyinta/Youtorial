import React, { useState, useEffect, useContext } from "react";
// import { Scrollbars } from "react-custom-scrollbars";
import { Slide, Paper } from "@mui/material";
import axios from "axios";
import Statistics from "./statistics";
import Loading from "../Utils/loading";
import { GlobalContext } from "../../../../context/globalContext";
import RenderStep from "../LearnPreview/renderStep";
import { Link } from "react-router-dom";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function LearnView() {
  const [tutorial, setTutorial] = useState({});
  const [loading, setLoading] = useState(false);

  const context = useContext(GlobalContext);
  const { stats, video } = context;

  useEffect(() => {
    if (video !== undefined && video !== "" && video !== null) {
      setLoading(false);
      axios
        .get(`${BASE_URL}/public/getPublicTutorial/${video.id}`)
        .then((res) => {
          if (res.status === 200) {
            if (res.data?.steps.length > 0) {
              setTutorial(res.data);
              setLoading(true);
            }
          } else if (res.status === 204) {
            setLoading(true);
            setTutorial(null);
          }
        })
        .catch((err) => console.log(err));
    }

    return function () {
      setTutorial({});
    };

    //eslint-disable-next-line
  }, [video]);

  return (
    <div id="scroll-container">
      {loading ? (
        tutorial != null && tutorial.steps != null ? (
          tutorial?.steps.map((step) => {
            return <Step step={step} key={step.id} stats={stats} />;
          })
        ) : (
          <div style={{ display: stats ? "none" : "block" }}>
            <p className="chooseTutorial">No tutorials yet for this video.</p>
            <p className="chooseTutorial">
              Go&nbsp;
              <Link to={`/video/edit-tutorial/${video?.id}`}>
                create
              </Link>{" "}
              &nbsp;one.
            </p>
          </div>
        )
      ) : (
        <Loading />
      )}

      <Slide direction="up" in={stats} mountOnEnter unmountOnExit>
        <Paper style={{ height: "100%" }}>
          <Statistics tutorial={tutorial} stats={stats} />
        </Paper>
      </Slide>
    </div>
  );
}

function Step({ step, stats }) {
  return (
    <div style={{ display: stats ? "none" : "block" }}>
      <RenderStep step={step} preview={false} />
    </div>
  );
}
