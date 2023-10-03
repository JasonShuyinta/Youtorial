import { Button } from "@mui/material";
import axios from "axios";
import React, { useContext } from "react";
import { GlobalContext } from "../../context/globalContext";

export default function Verify() {
  const { confirmationEmail, signedUpUserId } = useContext(GlobalContext);

  const resendVerification = () => {
    if (signedUpUserId) {
      axios
        .get(
          `${process.env.REACT_APP_SERVER_URL}/api/v1/user/resend-verification/${signedUpUserId}`
        )
        .then((res) => {
          if(res.status === 200) {
            console.log(res.data.id);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

  return (
    <div className="verify-container">
      <div className="verify-box">
        <h1 className="verify-title">Great, now verify your email!</h1>
        <p>
          Check your inbox at &nbsp;
          <b>{confirmationEmail}</b>&nbsp;and click the verification link inside
          to complete your registration.
        </p>
        <p>This link will expire shortly, so verify soon!</p>
        <p>
          <b>Don't see an email?&nbsp;</b>
          Check your spam folder.
        </p>
        <p>
          <b>Link expired?&nbsp;</b>
        </p>
        <Button variant="contained" onClick={() => resendVerification()} className="btn-style">
          Resend verification email
        </Button>
      </div>
    </div>
  );
}
