import React, { useState, useEffect, useContext } from "react";
import axios from "axios";
import {
  Grid,
  Avatar,
  Chip,
  TextField,
  Button,
  Menu,
  MenuItem,
} from "@mui/material";
import moment from "moment";
import "moment/locale/en-gb";
import { GlobalContext } from "../../../../../context/globalContext";
import { BiDotsVerticalRounded } from "react-icons/bi";
import { FaPencilAlt, FaTrashAlt } from "react-icons/fa";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function Comment({ comment, loggedUser, handleRefresh }) {
  const context = useContext(GlobalContext);
  const { config } = context;

  const [user, setUser] = useState({});
  const [openReply, setOpenReply] = useState(false);
  const [replyValue, setReplyValue] = useState("");
  const [anchorEl, setAnchorEl] = useState(null);
  const [modifyCommentSection, setModifyCommentSection] = useState(false);
  const [modifiedComment, setModifiedComment] = useState("");
  const [refresh, setRefresh] = useState(false);

  const open = Boolean(anchorEl);
  const handleCommentActions = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleCloseMenu = () => {
    setAnchorEl(null);
  };

  const handleModifyComment = () => {
    setModifyCommentSection(true);
    setModifiedComment(comment.commentText);
    handleCloseMenu();
  };

  const handleCloseCommentActions = () => {
    setModifyCommentSection(false);
  };

  const handleSaveModifiedComment = () => {
    axios
      .put(
        `${BASE_URL}/comment/${comment.id}`,
        { commentText: modifiedComment },
        config
      )
      .then((res) => {
        if (res.status === 200) {
          handleRefresh();
          setModifyCommentSection(false);
        }
      })
      .catch((err) => console.log(err));
  };

  useEffect(() => {
    axios
      .get(`${BASE_URL}/user/${comment.author}`)
      .then((res) => setUser(res.data))
      .catch((error) => console.log(error));
  }, [comment.author]);

  const handleReplyComment = () => {
    axios
      .post(
        `${BASE_URL}/comment/createComment`,
        {
          commentText: replyValue,
          videoId: comment.videoId,
          responseTo: comment.id,
        },
        config
      )
      .then((res) => {
        if (res.status === 200) handleRefresh();
      })
      .catch((err) => console.log(err));
    setReplyValue("");
    setOpenReply(false);
  };

  const handleDeleteComment = () => {
    axios
      .delete(`${BASE_URL}/comment/${comment.id}`, config)
      .then((res) => {
        if (res.status === 200) setRefresh(!refresh);
      })
      .catch((err) => console.log(err));
  };

  return (
    <Grid container style={{ padding: "0.5rem" }}>
      <Grid item xs={12}>
        <div style={{ display: "flex", alignItems: "center" }}>
          <Avatar src={user.image} alt={user.username} />
          <Chip label={user.username} className="chipStyle" />
          <span className="timeagoStyle">
            {moment(comment.createdAt).fromNow()}
          </span>
          <div style={{ flexGrow: "1" }} />
          <div>
            <BiDotsVerticalRounded
              onClick={handleCommentActions}
              size="1.5em"
              className="comment-action-btn"
            />
            <Menu
              id="basic-menu"
              anchorEl={anchorEl}
              open={open}
              onClose={handleCloseMenu}
            >
              <MenuItem onClick={handleModifyComment}>
                {" "}
                <FaPencilAlt style={{ marginRight: "0.5rem" }} /> Modify
              </MenuItem>
              <MenuItem onClick={handleDeleteComment}>
                {" "}
                <FaTrashAlt style={{ marginRight: "0.5rem" }} /> Delete
              </MenuItem>
            </Menu>
          </div>
        </div>
        {modifyCommentSection ? (
          <div style={{ padding: "0.5rem 0.5rem 0.5rem 3rem" }}>
            <TextField
              multiline
              fullWidth
              variant="standard"
              value={modifiedComment}
              onChange={(e) => setModifiedComment(e.target.value)}
            />
            <div className="comment-action-btn-container">
              <Button
                onClick={() => handleCloseCommentActions()}
                variant="contained"
                className="modify-comment-btn"
              >
                Cancel
              </Button>
              <Button
                onClick={() => handleSaveModifiedComment()}
                variant="contained"
                className={
                  modifiedComment === comment.commentText
                    ? "disable-save-btn"
                    : "btn-style"
                }
                disabled={modifiedComment === comment.commentText}
              >
                Save
              </Button>
            </div>
          </div>
        ) : (
          <p className="commentStyle">{comment.commentText}</p>
        )}
        <div className="likeDiv">
          <div className="center-and-align">
            <Button
              className="btn-style"
              onClick={() => setOpenReply(!openReply)}
              style={{ color: openReply ? "grey" : "black" }}
            >
              {" "}
              REPLY{" "}
            </Button>
          </div>
        </div>

        {openReply && (
          <div style={{ display: "flex", marginTop: "1rem" }}>
            <Avatar
              src={user.image}
              alt={user.username}
              style={{ margin: "1rem 1rem" }}
            />
            <TextField
              value={replyValue}
              onChange={(e) => setReplyValue(e.target.value)}
              autoFocus
              variant="standard"
              label="Reply to comment"
              fullWidth
              style={{ marginRight: "1rem" }}
            />
            <Button
              onClick={handleReplyComment}
              className="btn-style"
              style={{ margin: "1rem" }}
            >
              Publish
            </Button>
          </div>
        )}
      </Grid>
    </Grid>
  );
}
