import React, { useState, useEffect } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  TextField,
  Avatar,
  DialogActions,
  Button,
  Slide,
  FormHelperText,
} from "@mui/material";
import axios from "axios";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function DialogProfile({
  user,
  setUser,
  openProfileDialog,
  handleCloseProfileDialog,
}) {
  const [username, setUsername] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [image, setImage] = useState("");
  const [invalidUsername, setInvalidUsername] = useState(false);

  useEffect(() => {
    setUsername(user?.username);
    setFirstName(user?.firstName);
    setLastName(user?.lastName);
    setImage(user?.image);
  }, [user]);

  const handleAvatar = (e) => {
    setImage(URL.createObjectURL(e.target.files[0]));
    var reader = new FileReader();
    reader.readAsDataURL(e.target.files[0]);
    reader.onload = function (e) {
      //setThumbnailServer(reader.result);
    };
  };

  const handleUpdateProfile = () => {
    axios
      .put(`${BASE_URL}/user`, {
        id: user.id,
        email: user.email,
        firstName,
        lastName,
        username,
        image,
      })
      .then((res) => {
        setUser(res.data);
        setInvalidUsername(false);
        handleCloseProfileDialog();
      })
      .catch((e) => {
        if (e.response.status === 400) setInvalidUsername(true);
      });
  };

  return (
    <Dialog
      open={openProfileDialog}
      TransitionComponent={Transition}
      keepMounted
      onClose={handleCloseProfileDialog}
    >
      <DialogTitle style={{ padding: "2rem" }}>Edit your profile</DialogTitle>
      <DialogContent style={{ padding: "3rem" }}>
        <br></br>
        <div style={{ marginBottom: "1rem" }} className="center">
          <label>
            <Avatar
              src={image || ""}
              alt="Avatar"
              className="avatar-dialog-profile"
            />
            <input
              type="file"
              name="file"
              accept="image/*"
              defaultValue=""
              style={{ display: "none" }}
              onChange={handleAvatar}
            />
          </label>
          <div className="username-dialog-container">
            <TextField
              label="Username"
              focused
              value={username || ""}
              onChange={(e) => setUsername(e.target.value)}
            />
            {invalidUsername && (
              <>
                <FormHelperText style={{ color: "red" }}>
                  Username already in use
                </FormHelperText>
              </>
            )}
          </div>
        </div>
      </DialogContent>
      <DialogActions className="dialog-actions">
        <Button
          variant="contained"
          onClick={handleUpdateProfile}
          className="btn-style"
        >
          Save
        </Button>
        <Button
          variant="contained"
          onClick={handleCloseProfileDialog}
          className="btn-style"
        >
          Close
        </Button>
      </DialogActions>
    </Dialog>
  );
}
