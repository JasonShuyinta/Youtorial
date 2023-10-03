import React, { useState, useContext } from "react";
import { Avatar, Button, Grid, TextField } from "@mui/material";
import CommentList from "./CommentList/commentList";
import axios from "axios";
import { GlobalContext } from "../../../context/globalContext";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function Comments() {
  const [newComment, setNewComment] = useState("");
  const [refresh, setRefresh] = useState(false);

  const context = useContext(GlobalContext);
  const { config, loggedUser, video } = context;

  const handleNewComment = () => {
    setNewComment("");
    axios
      .post(
        `${BASE_URL}/comment/createComment`,
        {
          commentText: newComment,
          videoId: video.id,
        },
        config
      )
      .then((res) => {
        if (res.status === 200) setRefresh(!refresh);
      })
      .catch((err) => console.log(err));
  };

  const handleRefresh = () => {
    setRefresh(!refresh);
  };

  return (
    <section className="commentSection">
      <Grid container>
        <Grid item xs={12}>
          <h3 style={{ marginTop: "2rem" }}>Comments</h3>
        </Grid>
        <Grid item xs={12}>
          <div className="leaveComment">
            <Avatar
              src={loggedUser?.image}
              alt="avatar"
              className="avatarContainerComment"
            />
            <TextField
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
              multiline
              label="Leave a comment"
              fullWidth
              variant="standard"
            />
            <Button
              variant="contained"
              onClick={handleNewComment}
              className="publish-btn"
              style={{
                backgroundColor: loggedUser ? "black" : "grey",
                color: loggedUser ? "white" : "darkgrey",
              }}
            >
              Publish
            </Button>
          </div>
        </Grid>
        <Grid item xs={12}>
          <CommentList
            videoId={video?.id}
            refresh={refresh}
            loggedUser={loggedUser}
            handleRefresh={handleRefresh}
          />
        </Grid>
      </Grid>
    </section>
  );
}
