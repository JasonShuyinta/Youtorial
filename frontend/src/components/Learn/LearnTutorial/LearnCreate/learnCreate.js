import React, { useEffect, useContext } from "react";

import { Grid, Button } from "@mui/material";
import "react-quill/dist/quill.snow.css";
// import Scrollbars from "react-custom-scrollbars";
import { MdFullscreen } from "react-icons/md";

import FullScreenView from "./FullScreen/fullScreenView";
import { CreationContext } from "../../../../context/creationContext";
import { GlobalContext } from "../../../../context/globalContext";
import { useMediaQuery } from "react-responsive";
import CreateStep from "./createStep";

export default function LearnCreate() {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });

  const create = useContext(CreationContext);
  const { setFullScreen, updateMode, setUpdateMode, handleSubmit } =
    create;

  const context = useContext(GlobalContext);
  const { setShowSnack, video } = context;

  useEffect(() => {
    setTimeout(function () {
      setShowSnack(false);
    }, 5000);
  }, [setShowSnack]);

  return (
    <>
      {isTabletOrMobile ? (
        <div className="form-style-mobile">
          <div
            style={{
              display: updateMode ? "block" : "none",
            }}
          >
            <span onClick={() => setUpdateMode(false)}>Exit update mode</span>
          </div>
          <div className="center-and-end">
            <MdFullscreen
              onClick={() => setFullScreen(true)}
              className="fullscreen-icon"
            />
          </div>
          <Grid container>
            <CreateStep updateMode={updateMode} />
          </Grid>
          <div className="btn-save-container">
              <Button
                onClick={handleSubmit}
                variant="contained"
                className="btn-save"
                fullWidth
              >
                {updateMode ? "Update" : "Save"}
              </Button>
            </div>
          <FullScreenView video={video} />
        </div>
      ) : (
        <div style={{overflowX: "hidden", overflowY: "auto", height: "81vh"}}>
          <div className="form-style">
            <div
              className="update-mode-toggle"
              style={{
                display: updateMode ? "block" : "none",
              }}
            >
              <span onClick={() => setUpdateMode(false)}>Exit update mode</span>
            </div>
            <div className="update-mode-btn">
              <MdFullscreen
                onClick={() => setFullScreen(true)}
                className="fullscreen-icon"
              />
            </div>
            <Grid container>
              <CreateStep updateMode={updateMode} />
            </Grid>
            <div className="btn-save-container">
              <Button
                onClick={handleSubmit}
                variant="contained"
                className="btn-save"
                fullWidth
              >
                {updateMode ? "Update" : "Save"}
              </Button>
            </div>
            <FullScreenView video={video} />
          </div>
        </div>
      )}
    </>
  );
}
