import React, { useEffect, useState, useContext } from "react";
import {
  Grid,
  Paper,
  Typography,
  Avatar,
  IconButton,
  Dialog,
  DialogContent,
  DialogTitle,
  DialogActions,
  Button,
  DialogContentText,
} from "@mui/material";
import { Link } from "react-router-dom";
import { useMediaQuery } from "react-responsive";
import axios from "axios";
import { AiFillVideoCamera } from "react-icons/ai";
import { FaPencilAlt } from "react-icons/fa";
import DeleteIcon from "@mui/icons-material/Delete";
import { timeConverter } from "../utils/helper";
import moment from "moment"
import 'moment/locale/en-gb';
import { GlobalContext } from "../../context/globalContext";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function Video({ video }) {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 500px)" });

  const context = useContext(GlobalContext);
  const { successfulUpdate, setSuccessfulUpdate } = context;

  const [user, setUser] = useState("");
  const [duration, setDuration] = useState("");
  const [hoverEditVideo, setHoverEditVideo] = useState(false);
  const [hoverEditTutorial, setHoverEditTutorial] = useState(false);
  const [hoverDeleteVideo, setHoverDeleteVideo] = useState(false);
  const [openDialogDeleteVideo, setOpenDialogDeleteVideo] = useState(false);
  const [openConfirmationDialog, setOpenConfirmationDialog] = useState(false);

  useEffect(() => {
    if (video !== "" && video !== undefined && video !== null) {
      setDuration(timeConverter(video.duration));
      axios
        .get(`${BASE_URL}/user/${video.author}`)
        .then((res) => setUser(res.data))
        .catch((err) => console.log(err));
    }
  }, [video]);

  const handleDeleteVideo = () => {
    axios
      .delete(`${BASE_URL}/video/deleteVideo/${video.id}`)
      .then((res) => {
        if (res.status === 200) {
          setOpenDialogDeleteVideo(false);
          setOpenConfirmationDialog(true);
          setSuccessfulUpdate(!successfulUpdate);
        }
      })
      .catch((err) => console.log(err));
  };

  function RenderDesktop() {
    return (
      <Grid item xs={12} sm={6} md={3}>
        <div style={{ padding: "2rem 1rem" }}>
          <Link to={{ pathname: `/learn/${video?.id}` }}>
            <Paper className="hvr-underline-from-center ">
              <div className="img-container">
                <img src={video?.thumbnail} alt="thumbnail" />
              </div>
              <div className="info-container">
                <Typography variant="h6" className="video-title">
                  {video?.title}
                </Typography>
                <div className="author-container">
                  <Avatar src={user?.image} alt="avatar" />
                  <Typography variant="subtitle1">{user?.username}</Typography>
                </div>
                <Typography variant="subtitle1">{duration}</Typography>
                {video && video.category && (
                  <div className="category-container">
                    {video?.category?.length !== 0 &&
                      video?.category.map((cat) => {
                        return <span key={cat}>#{cat} &nbsp;</span>;
                      })}
                  </div>
                )}
                <div>
                  <Typography variant="body2">
                    Uploaded{" "}
                    <span
                      className="timeagoStyle"
                      >
                      {moment(video?.uploadDate).fromNow()}
                    </span>
                  </Typography>
                </div>
              </div>
            </Paper>
          </Link>
          <div className="top-edit-btn-container">
            <Link
              to={{ pathname: `/video/edit-tutorial/${video?.id}` }}
              className="hidelink"
            >
              <Paper
                className="center-and-start "
                elevation={hoverEditTutorial ? 3 : 1}
                onMouseEnter={() => setHoverEditTutorial(true)}
                onMouseLeave={() => setHoverEditTutorial(false)}
              >
                <IconButton className="edit-icon-btn">
                  <FaPencilAlt />
                </IconButton>
                <span>Edit tutorial</span>
              </Paper>
            </Link>
          </div>
          <div className="bottom-edit-btn-container">
            <Paper
              className="center-and-start w-50 bottom-edit-btn"
              elevation={hoverDeleteVideo ? 3 : 1}
              onMouseEnter={() => setHoverDeleteVideo(true)}
              onMouseLeave={() => setHoverDeleteVideo(false)}
              onClick={() => setOpenDialogDeleteVideo(true)}
            >
              <IconButton className="delete-icon-btn-container">
                <DeleteIcon />
              </IconButton>
              <span>Delete video</span>
            </Paper>
            <Link
              to={{
                pathname: `/video/edit-video/${video?.id}`,
              }}
              className="hidelink w-50 bottom-edit-btn"
            >
              <Paper
                className="center-and-start"
                elevation={hoverEditVideo ? 3 : 1}
                onMouseEnter={() => setHoverEditVideo(true)}
                onMouseLeave={() => setHoverEditVideo(false)}
              >
                <IconButton style={{ color: "black" }}>
                  <AiFillVideoCamera />
                </IconButton>
                <span>Edit video</span>
              </Paper>
            </Link>
          </div>
        </div>
      </Grid>
    );
  }

  function RenderMobile() {
    return (
      <Grid item xs={12} className="center pb-4">
        <div>
          <Link to={{ pathname: `/learn/${video?.id}` }}>
            <Paper elevation={1} className="hvr-underline-from-center">
              <div className="img-container-mobile">
                <img src={video?.thumbnail} alt="thumbnail" />
              </div>
              <div style={{ padding: "3vw" }}>
                <Typography variant="h6" className="video-title">
                  {video?.title}
                </Typography>
                <div className="author-container">
                  <Avatar src={user?.image} alt="avatar" />
                  <Typography variant="subtitle1">{user?.username}</Typography>
                </div>
                <Typography variant="subtitle1">{duration}</Typography>
                {video && video.category && (
                  <div className="category-container">
                    {video?.category.length !== 0 &&
                      video?.category.map((cat) => {
                        return <span key={cat}> #{cat} &nbsp;</span>;
                      })}
                  </div>
                )}
                <div>
                  <Typography variant="body2">
                    Uploaded{" "}
                    <span
                      className="timeagoStyle"
                      >
                      {moment(video?.uploadDate).fromNow()}
                    </span>
                  </Typography>
                </div>
              </div>
            </Paper>
          </Link>
          <div className="top-edit-btn-container">
            <Link
              to={{ pathname: `/video/edit-tutorial/${video?.id}` }}
              className="hidelink"
              style={{ width: "50%" }}
            >
              <Paper
                className="center-and-start"
                style={{ height: "3rem" }}
                elevation={hoverEditTutorial ? 3 : 1}
                onMouseEnter={() => setHoverEditTutorial(true)}
                onMouseLeave={() => setHoverEditTutorial(false)}
              >
                <IconButton style={{ color: "black" }}>
                  <FaPencilAlt />
                </IconButton>
                <span>Edit tutorial</span>
              </Paper>
            </Link>
          </div>
          <div className="bottom-edit-btn-container ">
            <Paper
              className="center-and-start w-50"
              style={{ height: "3rem" }}
              elevation={hoverEditVideo ? 3 : 1}
              onMouseEnter={() => setHoverEditVideo(true)}
              onMouseLeave={() => setHoverEditVideo(false)}
              onClick={() => setOpenDialogDeleteVideo(true)}
            >
              <IconButton style={{ color: "black" }}>
                <DeleteIcon />
              </IconButton>
              <span>Delete video</span>
            </Paper>
            <Link
              to={{ pathname: `/video/edit-tutorial/${video?.id}` }}
              className="hidelink"
              style={{ width: "50%" }}
            >
              <Paper
                className="center-and-start"
                style={{ height: "3rem" }}
                elevation={hoverEditTutorial ? 3 : 1}
                onMouseEnter={() => setHoverEditTutorial(true)}
                onMouseLeave={() => setHoverEditTutorial(false)}
              >
                <IconButton style={{ color: "black" }}>
                  <FaPencilAlt />
                </IconButton>
                <span>Edit tutorial</span>
              </Paper>
            </Link>
          </div>
        </div>
      </Grid>
    );
  }
  return (
    <>
      {isTabletOrMobile ? <RenderMobile /> : <RenderDesktop />}
      <Dialog
        open={openDialogDeleteVideo}
        onClose={() => setOpenDialogDeleteVideo(false)}
      >
        <DialogTitle>Are you sure you want to delete this video?</DialogTitle>
        <DialogContent>
          <DialogContentText>
            If you delete this video, your tutorial will be deleted, as well as
            all of the comments.
            <br></br>
            This action is <b style={{ color: "red" }}>irreversible.</b>
            <br></br>
            Do you want to delete this video?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => setOpenDialogDeleteVideo(false)}
            style={{ backgroundColor: "red" }}
            variant="contained"
          >
            No
          </Button>
          <Button
            onClick={() => handleDeleteVideo()}
            style={{ backgroundColor: "green" }}
            variant="contained"
          >
            Yes
          </Button>
        </DialogActions>
      </Dialog>
      <Dialog
        open={openConfirmationDialog}
        onClose={() => setOpenConfirmationDialog(false)}
      >
        <DialogContent>
          <DialogContentText>
            Your video was successfully deleted!
          </DialogContentText>
        </DialogContent>
      </Dialog>
    </>
  );
}
