import React, { useState, useEffect, useContext } from "react";
import { Grid, LinearProgress, Paper, Typography } from "@mui/material";
import moment from "moment";
import axios from "axios";
import { GlobalContext } from "../../../../context/globalContext";
import Unlogged from "../../../Account/unlogged";
import Loading from "../Utils/loading";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function Statistics({ tutorial, stats }) {
  const [avgStepTime, setAvgStepTime] = useState("");
  const [completionFraction, setCompletionFraction] = useState("");
  const [completionPercentage, setCompletionPercentage] = useState("");
  const [loading, setLoading] = useState(false);

  const context = useContext(GlobalContext);
  const { config, loggedUser, video } = context;

  useEffect(() => {
    if (loggedUser && config) {
      if (tutorial?.steps) {
        var durationSum = 0;
        tutorial?.steps.forEach((element) => {
          var start, end;
          if (element.startTime.length === 5) start = "00:" + element.startTime;
          if (element.endTime.length === 5) end = "00:" + element.endTime;
          durationSum += moment.duration(
            moment.duration(end) - moment.duration(start)
          );
        });

        setAvgStepTime(moment.duration(durationSum / tutorial.steps.length));

        if (video) {
          setLoading(false);
          axios
            .get(
              `${BASE_URL}/interaction/getCompletionPercentage/${video.id}`,
              config
            )
            .then((res) => {
              if (res.status === 200) {
                setCompletionFraction(`${res.data}/${tutorial?.steps.length}`);
                setCompletionPercentage(
                  `${Math.trunc((res.data * 100) / tutorial?.steps.length)}`
                );
                setLoading(true);
              }
            })
            .catch((err) => console.log(err));
        }
      } else setLoading(true);
    }
    //eslint-disable-next-line
  }, [stats, tutorial, config]);

  return (
    <>
      <Paper className="paperStats">
        {loggedUser ? (
          <>
            {loading ? (
              <Grid container>
                <Grid item xs={12}>
                  <Typography variant="h5"> Statistics </Typography>
                  {avgStepTime ? (
                    <Typography variant="subtitle1">
                      <b>Average time per step: &nbsp;</b>
                      {avgStepTime?._data?.hours === 0
                        ? ""
                        : avgStepTime?._data?.hours + "h"}{" "}
                      &nbsp;
                      {avgStepTime?._data?.minutes + "m"} &nbsp;
                      {avgStepTime?._data?.seconds + "s"}
                    </Typography>
                  ) : (
                    <Typography variant="subtitle1" className="center-and-align">
                      No tutorial is present
                    </Typography>
                  )}
                </Grid>
                <Grid item xs={11}>
                  {completionFraction && (
                    <>
                      <Typography variant="subtitle1">
                        Completed {completionFraction} steps &nbsp; (
                        {completionPercentage}
                        %)
                      </Typography>
                      <LinearProgress
                        variant="determinate"
                        value={parseInt(completionPercentage)}
                      />
                    </>
                  )}
                </Grid>
              </Grid>
            ) : (
              <Loading />
            )}
          </>
        ) : (
          <Unlogged />
        )}
      </Paper>
    </>
  );
}
