import {
  Avatar,
  Divider,
  Grid,
  Typography,
  Button,
  Dialog,
  DialogContent,
  DialogContentText,
  DialogTitle,
  TextField,
  DialogActions,
  Box,
  Container,
} from "@mui/material";
import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { GlobalContext } from "../../context/globalContext";
import DialogProfile from "./dialogProfile";
import moment from "moment";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function Profile() {
  const [user, setUser] = useState(null);
  // const [followers, setFollowers] = useState(0);
  const [openProfileDialog, setOpenProfileDialog] = useState(false);
  const [openPasswordChangeDialog, setOpenPasswordChangeDialog] =
    useState(false);
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [passwordMatching, setPasswordMatching] = useState(true);
  const [passwordInvalid, setPasswordInvalid] = useState(false);

  const { config } = useContext(GlobalContext);

  useEffect(() => {
    axios
      .get(`${BASE_URL}/user/getUserByToken`, config)
      .then((res) => {
        setUser(res.data);

        // axios
        //   .get(`${BASE_URL}/interaction/getFollowersNumber`, config)
        //   .then((res) => setFollowers(res.data))
        //   .catch((err) => console.log(err));
      })
      .catch((err) => console.log(err));
    //eslint-disable-next-line
  }, []);

  const openUpdateProfileDialog = () => {
    setOpenProfileDialog(true);
  };

  const handleCloseProfileDialog = () => {
    setOpenProfileDialog(false);
  };

  const handleChangePassword = () => {
    if (newPassword !== confirmPassword) {
      setPasswordMatching(false);
      return;
    }
    setPasswordMatching(true);
    axios
      .post(
        `${BASE_URL}/user/change-password`,
        { currentPassword: currentPassword, newPassword: newPassword },
        config
      )
      .then((res) => {
        if (res.data) {
          setPasswordInvalid(false);
          setOpenPasswordChangeDialog(false);
          setCurrentPassword("");
          setNewPassword("");
          setConfirmPassword("");
          alert("Password changed successfully");
        } else {
          setPasswordInvalid(true);
        }
      })
      .catch((err) => {
        if (err.status === 400) console.log("Current password is wrong");
      });
  };

  return (
    <>
      <div className="full-height">
        <Container>
          <Box>
            <Grid container className="profile-info">
              <Grid item xs={12} md={6} className="avatar-profile-container">
                <div className="profile-avatar-followers">
                  <Avatar
                    src={user?.image}
                    alt="avatar"
                    className="profile-avatar"
                  />
                  <div className="followers-container">
                    <div>
                      <Typography variant="h5">{user?.username}</Typography>
                      
                      {/* <button className="follower-btn">
                        {" "}
                        Followers
                        <span class="followers">&nbsp; {followers} </span>
                      </button> */}
                    </div>
                  </div>
                </div>
              </Grid>
              <Grid item xs={12} md={6} className="edit-profile-btn">
                <Button
                  onClick={() => setOpenPasswordChangeDialog(true)}
                  variant="contained"
                  className="btn-style"
                  style={{ marginRight: "1rem" }}
                >
                  Change password
                </Button>
                <Button
                  onClick={() => openUpdateProfileDialog()}
                  variant="contained"
                  className="btn-style"
                >
                  Edit profile
                </Button>
              </Grid>
            </Grid>
            <Divider />
            <Grid container className="profile-container">
              <Grid item xs={12} md={6} className="profile-container-grid">
                <Typography variant="h5">First name</Typography>
                <Typography variant="h6">{user?.firstName}</Typography>
              </Grid>
              <Grid item xs={12} md={6} className="profile-container-grid">
                <Typography variant="h5">Last name</Typography>
                <Typography variant="h6">{user?.lastName}</Typography>
              </Grid>
              <Grid item xs={12} md={6} className="profile-container-grid">
                <Typography variant="h5">Email</Typography>
                <Typography variant="h6">{user?.email}</Typography>
              </Grid>
              <Grid item xs={12} md={6} className="profile-container-grid">
                <Typography variant="h5">Subscription date</Typography>
                <Typography variant="h6">
                  {moment(user?.subscriptionDate).format(
                    "MMMM Do YYYY, h:mm a"
                  )}
                </Typography>
              </Grid>
            </Grid>
          </Box>
        </Container>
      </div>
      <DialogProfile
        user={user}
        setUser={setUser}
        openProfileDialog={openProfileDialog}
        handleCloseProfileDialog={handleCloseProfileDialog}
      />
      <Dialog
        open={openPasswordChangeDialog}
        onClose={() => setOpenPasswordChangeDialog(false)}
      >
        <DialogTitle>Change password</DialogTitle>
        <DialogContent>
          <DialogContentText style={{ padding: "1rem 0" }}>
            To change your password, please enter your current password and then
            enter your new password twice.
          </DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            id="currentPassword"
            label="Current password"
            type="password"
            fullWidth
            value={currentPassword || ""}
            onChange={(e) => setCurrentPassword(e.target.value)}
            required
          />
          <TextField
            margin="dense"
            id="newPassword"
            label="New password"
            type="password"
            fullWidth
            value={newPassword || ""}
            onChange={(e) => setNewPassword(e.target.value)}
            required
          />
          <TextField
            margin="dense"
            id="newPasswordConfirmation"
            label="New password confirmation"
            type="password"
            fullWidth
            value={confirmPassword || ""}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
          />
          <div>
            {!passwordMatching && (
              <span className="error-msg">
                Passwords do not match. Please try again.
              </span>
            )}
          </div>
          <div>
            {passwordInvalid && (
              <span className="error-msg">
                Current password is wrong. Please try again.
              </span>
            )}
          </div>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => setOpenPasswordChangeDialog(false)}
            className="btn-style"
          >
            Cancel
          </Button>
          <Button onClick={handleChangePassword} className="btn-style">
            Change password
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
