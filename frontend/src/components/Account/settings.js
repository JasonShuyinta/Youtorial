import {
  Box,
  Container,
  Divider,
  Grid,
  TextField,
  Typography,
  InputAdornment,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Button,
} from "@mui/material";
import React, { useState, useContext } from "react";
import { FcSettings } from "react-icons/fc";
import { TiTick, TiTimes } from "react-icons/ti";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import { GlobalContext } from "../../context/globalContext";
import axios from "axios";
import { useHistory } from "react-router-dom";

export default function Settings() {
  const history = useHistory();
  const context = useContext(GlobalContext);
  const { loggedUser } = context;

  const [confirmDelete, setConfirmDelete] = useState(false);
  const [showPwd, setShowPwd] = useState(false);
  const [password, setPassword] = useState("");
  const [passwordError, setPasswordError] = useState("");

  const handleCloseConfirmDelete = () => {
    setConfirmDelete(false);
  };

  const handleCopyToClip = (userCode) => {
    navigator.clipboard.writeText(userCode);
  };

  const handleDeleteAccount = () => {
    axios
      .post(
        `${process.env.REACT_APP_SERVER_URL}/api/v1/user/delete`,
        context.config,
        {
          id: loggedUser.id,
          password,
        }
      )
      .then((res) => {
        if (res.data) {
          localStorage.removeItem("accesstoken");
          context.setLoggedUser(null);
          context.setConfig(null);
          history.push("/");
        } else {
          setPasswordError("Password is incorrect");
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <>
      <div className="p-2 full-wdith-height center-and-align">
        <Container>
          <Box>
            <div className="p-1">
              <Typography variant="h6">Settings</Typography>
              <Grid container className="mt-2">
                <Grid item xs={6}>
                  <Typography variant="h4">
                    Handle your account settings
                  </Typography>
                </Grid>
                <Grid item xs={6} className="center-and-end">
                  <FcSettings style={{ width: "150px", height: "150px" }} />
                </Grid>
              </Grid>
            </div>
          </Box>
          <Divider />
          <Box className="center-and-align p-2 mt-2">
            <Grid container spacing={3}>
              <Grid item xs={4} className="center-and-align p-1">
                <Typography variant="h6">Account ID</Typography>
              </Grid>
              <Grid item xs={8} className="p1">
                <TextField
                  variant="outlined"
                  disabled
                  fullWidth
                  value={loggedUser?.userCode}
                  InputProps={{
                    endAdornment: (
                      <InputAdornment
                        style={{ cursor: "pointer" }}
                        position="end"
                        onClick={handleCopyToClip(loggedUser?.userCode)}
                      >
                        <span className="copy-user-code">COPY</span>
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>
              <Grid item xs={4} className="center-and-align p-1">
                <Typography variant="h6">Enabled</Typography>
              </Grid>
              <Grid item xs={8} className="p-1">
                <Typography variant="h6">
                  {loggedUser?.enabled ? (
                    <TiTick className="enabled-icon-true" />
                  ) : (
                    <TiTimes className="enabled-icon-false" />
                  )}
                </Typography>
              </Grid>
              <Grid item xs={4} className="center-and-align p-1">
                <Typography variant="h6">Delete account</Typography>
              </Grid>
              <Grid item xs={8} className="p-1">
                <Typography variant="h6">
                  <span
                    className="delete-account"
                    onClick={() => setConfirmDelete(true)}
                  >
                    DELETE
                  </span>
                </Typography>
              </Grid>
            </Grid>
          </Box>
        </Container>
      </div>

      <Dialog open={confirmDelete} onClose={handleCloseConfirmDelete}>
        <DialogTitle>Delete account</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to delete your account?
            <br></br>
            All the data about your account will be deleted.
            <br></br>
            This action is <b>irreversible.</b>
            <br></br>
            <span className="mt-2">
              Insert your password to confirm your account deletion.
            </span>
          </DialogContentText>
          <TextField
            className="mt-1"
            label="Password"
            fullWidth
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            type={showPwd ? "text" : "password"}
            InputProps={{
              endAdornment: (
                <InputAdornment
                  style={{ cursor: "pointer" }}
                  position="end"
                  onClick={() => setShowPwd(!showPwd)}
                >
                  {showPwd ? (
                    <VisibilityOff style={{ color: "gray" }} />
                  ) : (
                    <Visibility style={{ color: "gray" }} />
                  )}
                </InputAdornment>
              ),
            }}
          />
          {passwordError && <span className="error-msg">{passwordError}</span>}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseConfirmDelete}>Cancel</Button>
          <Button onClick={handleDeleteAccount}>Delete Account</Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
