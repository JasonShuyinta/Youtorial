import { Button } from "@mui/material";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { logo } from "../utils/constants";

export default function Verified() {
  const [verified, setVerified] = useState(false);
  const { verificationCode } = useParams();

  useEffect(() => {
    axios
      .get(
        `${process.env.REACT_APP_SERVER_URL}/api/v1/user/verify/${verificationCode}`
      )
      .then((res) => {
        setVerified(res.data);
        console.log(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [verificationCode]);

  return (
    <div className="min-height-container">
      {verified ? (
        <div style={{ textAlign: "center" }}>
          <div className="center-and-align">
            <img src={logo} alt="logo.png" width="150px" height="150px" />
          </div>
          <h1>Email successfully verified!</h1>
          <p>You can now login into your account.</p>
          <a href="https://youtorial.org/account/login" className="hidelink">
            <Button className="btn-style">Continue</Button>
          </a>
        </div>
      ) : (
        <div style={{ padding: "4rem" }}>
          <div className="center-and-align">
            <img
              src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/Youtorial_FLAT_JH__ref_jh__07-nobg.png"
              alt="logo.png"
              width="150px"
              height="150px"
            />
          </div>
          <div style={{ textAlign: "center" }}>
            <h3>Sorry, we could not verify account &#128532;</h3>
            <h3>
              It may have been already verified, or the verification code is
              incorrect.
            </h3>
            <a href="https://youtorial.org/account/login">Try again</a>
          </div>
        </div>
      )}
    </div>
  );
}
