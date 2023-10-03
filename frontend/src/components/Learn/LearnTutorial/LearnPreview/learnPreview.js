import React, { useState, useEffect, useContext } from "react";
import axios from "axios";
import { GlobalContext } from "../../../../context/globalContext";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from "@mui/material";
import { CreationContext } from "../../../../context/creationContext";
import { useMediaQuery } from "react-responsive";
import RenderStep from "./renderStep";
import Loading from "../Utils/loading";
// import Scrollbars from "react-custom-scrollbars";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function LearnPreview({ currentStep }) {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });

  const [loading, setLoading] = useState(false);
  const [steps, setSteps] = useState([]);
  const [tutorial, setTutorial] = useState({});
  const [zeroSteps, setZeroSteps] = useState(false);
  const [empty, setEmpty] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [deleteStepId, setDeleteStepId] = useState("");
  const [successfullyDeleted, setSuccessfullyDeleted] = useState(false);

  const context = useContext(GlobalContext);
  const {
    video,
    checkDifferences,
    config,
    isSame,
    setIsSame,
    setShowSnack,
    setSnackMsg,
  } = context;

  const create = useContext(CreationContext);
  const { setStartTime, updateTitle } = create;

  useEffect(() => {
    setLoading(false);
    if (video) {
      axios
        .get(`${BASE_URL}/tutorial/getSelectedTutorial/${video.id}`, config)
        .then((res) => {
          if (res.status === 204) {
            setEmpty(true);
            setLoading(true);
          }
          if (res.status === 200) {
            if (res.data.steps.length === 0) setZeroSteps(true);
            else {
              setEmpty(false);
              setZeroSteps(false);
              setSteps(res.data.steps);
              setTutorial(res.data);
              checkDifferences(res.data.id);
              setLoading(true);
            }
          }
        })
        .catch((err) => console.log(err));
    }
    // eslint-disable-next-line
  }, [currentStep, successfullyDeleted, isSame]);

  const handleDeleteStep = (id) => {
    setOpenDelete(true);
    setDeleteStepId(id);
  };

  const handleDelete = () => {
    axios
      .post(
        `${BASE_URL}/tutorial/deleteStep`,
        { stepId: deleteStepId, tutorialId: tutorial.id, videoId: video.id },
        config
      )
      .then((res) => {
        if (res.status === 200) {
          setSuccessfullyDeleted(!successfullyDeleted);
          if (res.data.steps.length > 0) {
            setStartTime(res.data.steps[res.data.steps.length - 1].endTime);
          } else setStartTime("00:00");
          updateTitle();
        }
      })
      .catch((err) => console.log(err));
    handleCloseDialog();
  };
  const handleCloseDialog = () => {
    setOpenDelete(false);
  };

  const publishTutorial = () => {
    axios
      .post(`${BASE_URL}/public/publish`, {
        id: tutorial.id,
        videoId: tutorial.videoId,
        author: video.author,
        steps: tutorial.steps,
      })
      .then((res) => {
        if (res.status === 200) {
          setShowSnack(true);
          setSnackMsg("Your tutorial has been published!");
          setIsSame(true);
        }
      })
      .catch((err) => console.log(err));
  };

  return (
    <>
      <div className="preview-container">
        {empty || zeroSteps ? (
          <p className="chooseTutorial">You haven't any step saved!</p>
        ) : loading ? (
           <div style={{overflowX: "hidden", overflowY: "auto", height: "100%"}}>
            <div
              style={{
                minHeight: isTabletOrMobile && "42vh",
                overflowY: "auto",
              }}
            >
              {steps.map((step) => {
                return (
                  <RenderStep
                    step={step}
                    preview={true}
                    handleDeleteStep={handleDeleteStep}
                    key={step.id}
                  />
                );
              })}
            </div>
           </div>
        ) : (
          <Loading />
        )}
        <div style={{ width: "100%" }}>
          <Button
            disabled={!steps.length || isSame}
            onClick={publishTutorial}
            variant="contained"
            className="btnPublish"
            fullWidth
            style={{
              display: empty || zeroSteps ? "none" : "block",
              backgroundColor: !steps.length || isSame ? "gray" : "#181818",
              paddingTop: "0.3rem", paddingBottom: "0.3rem"
            }}
          >
            {isSame ? "Already up to date!" : "Publish"}
          </Button>
        </div>
      </div>

      <Dialog
        open={openDelete}
        onClose={handleCloseDialog}
        style={{ padding: "4rem" }}
      >
        <DialogTitle>Are you sure you want to delete this step? </DialogTitle>
        <DialogContent>
          <DialogContentText>This action is irreversible</DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDelete} className="deleteBtns">
            Delete
          </Button>
          <Button onClick={handleCloseDialog} className="deleteBtns">
            Cancel
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
