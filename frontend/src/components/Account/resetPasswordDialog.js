import React, { useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
  TextField,
} from "@mui/material";
import axios from "axios"

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function ResetPasswordDialog({
  openResetPasswordDialog,
  setOpenResetPasswordDialog,
  setEmailSent
}) {

  const [email, setEmail] = useState("");

  const handleEmailLink = () => {
    axios.get(`${BASE_URL}/user/reset-password/${email}`)
    .then((res) => {
      if(res.status === 200) setEmailSent(true);
    })
    .catch((err) => { console.log(err) });
    setOpenResetPasswordDialog(false)
  }

  return (
    <Dialog
      open={openResetPasswordDialog}
      onClose={() => setOpenResetPasswordDialog(false)}
      maxWidth="sm"
      fullWidth
    >
      <div className="reset-password-dialog">
        <DialogTitle>Forgot Password? </DialogTitle>
        <DialogContent>
          <DialogContentText>
              You can reset your password here.
          </DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            id="name"
            label="Email Address"
            type="email"
            fullWidth
            variant="standard"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleEmailLink} className="btn-style">
            Send email
          </Button>
        </DialogActions>
      </div>
    </Dialog>
  );
}
