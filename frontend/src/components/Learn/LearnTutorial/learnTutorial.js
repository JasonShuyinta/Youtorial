import React, { useEffect, useContext } from "react";
import { Grid, Paper, Tooltip } from "@mui/material";
import Snack from "../../utils/snack";
import { RiNumbersFill } from "react-icons/ri";
import { CreationProvider } from "../../../context/creationContext";
import { GlobalContext } from "../../../context/globalContext";
import LearnView from "./LearnView/learnView";
import { useMediaQuery } from "react-responsive";

export default function LearnTutorial({ videoAuthor }) {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });

  const context = useContext(GlobalContext);
  const {
    stats,
    setStats,
    showSnack,
    snackMsg,
    setSelectedTutor,
    loggedUser,
    setLoggedUserIsAuthor,
  } = context;

  useEffect(() => {
    if (loggedUser) {
      if (videoAuthor.id === loggedUser.id) {
        setLoggedUserIsAuthor(true);
        setSelectedTutor(loggedUser.id);
      }
    } else setLoggedUserIsAuthor(false);
    // eslint-disable-next-line
  }, [videoAuthor, loggedUser]);

  return (
    <>
      <CreationProvider>
        {isTabletOrMobile ? (
          <div className="tutorialSectionMobile">
            <Paper square className="center-and-align">
              <Grid container>
                <Grid item xs={6} style={{ fontSize: "2vh" }}>
                  <p>
                    <b>Steps</b>
                  </p>
                </Grid>
                <Grid item xs={6} className="stats">
                  <div style={{ cursor: "pointer" }}>
                    <Tooltip
                      title={"Check statistics for this tutorial"}
                      placement="left-start"
                    >
                      <span>
                        <RiNumbersFill
                          onClick={() => setStats(!stats)}
                          style={{
                            color: stats ? "black" : "gray",
                            transform: stats ? "scale(1.3)" : "scale(1.0)",
                            marginRight: "2vw",
                          }}
                          size={20}
                        />
                      </span>
                    </Tooltip>
                  </div>
                </Grid>
              </Grid>
            </Paper>
            <div
              className="tutorialSectionContainer"
              style={{ overflowY: "auto" }}
            >
              <LearnView />
            </div>

            <Snack message={snackMsg} successful={showSnack} />
          </div>
        ) : (
          <div className="tutorialSection">
            <Paper square className="paperHeader">
              <Grid container>
                <Grid item xs={6}>
                  <p>
                    <b>Steps</b>
                  </p>
                </Grid>
                <Grid item xs={6} className="center-and-end">
                  <div>
                    <Tooltip
                      title={"Check statistics for this tutorial"}
                      placement="left-start"
                    >
                      <span>
                        <RiNumbersFill
                          onClick={() => setStats(!stats)}
                          style={{
                            color: stats ? "black" : "gray",
                            transform: stats ? "scale(1.3)" : "scale(1.0)",
                            marginRight: "1vw",
                          }}
                          size={20}
                        />
                      </span>
                    </Tooltip>
                  </div>
                </Grid>
              </Grid>
            </Paper>
            <div className="learnview-container">
              <LearnView />
            </div>

            <Snack message={snackMsg} successful={showSnack} />
          </div>
        )}
      </CreationProvider>
    </>
  );
}
