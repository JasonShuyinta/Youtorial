import React, { useState, useContext } from "react";
import {
  Grid,
  Typography,
  InputAdornment,
  TextField,
  Button,
  Dialog,
  DialogContent,
  DialogContentText,
  DialogActions,
  DialogTitle,
} from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import axios from "axios";
import { GlobalContext } from "../../context/globalContext";
import { Link } from "react-router-dom";
import { useHistory } from "react-router-dom";
import { useMediaQuery } from "react-responsive";
import ResetPasswordDialog from "./resetPasswordDialog";
import { logo } from "../utils/constants";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function Login() {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });
  const [loginError, setLoginError] = useState("");
  const [showPwd, setShowPwd] = useState(false);
  const [openResetPasswordDialog, setOpenResetPasswordDialog] = useState(false);
  const [emailSent, setEmailSent] = useState(false);

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorEmail, setErrorEmail] = useState(false);
  const [errorPwd, setErrorPwd] = useState(false);
  const [errorEmailMsg, setErrorEmailMsg] = useState("Error");
  const [errorPwdMsg, setErrorPwdMsg] = useState("Error");

  const { setLoggedUser, setConfig } = useContext(GlobalContext);
  const history = useHistory();

  const handleSubmit = (event) => {
    event.preventDefault();
    var submitForm = true;
    const regexExp =
      /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/gi;

    if (!regexExp.test(email)) {
      setErrorEmail(true);
      setErrorEmailMsg("Invalid email address");
      submitForm = false;
    }

    if (password.length < 6) {
      setErrorPwd(true);
      setErrorPwdMsg("Password should be at least 6 characters");
      submitForm = false;
    }

    if (submitForm) {
      axios
        .post(`${BASE_URL}/user/login`, {
          email: email.toLowerCase(),
          password,
        })
        .then((res) => {
          setLoggedUser(res.data);
          localStorage.setItem("accesstoken", res.headers.accesstoken);
          setConfig({
            headers: {
              authorization: `Bearer ${localStorage.getItem("accesstoken")}`,
            },
          });
          history.push("/");
        })
        .catch((err) => {
          if (err.response.status === 404) setLoginError("Email doesn't exist");
          else if (err.response.status === 401)
            setLoginError("Invalid credentials");
          else setLoginError("Something went wrong");
        });
    }
  };

  return (
    <>
      <div className="log-container">
        <Grid container className="login-container">
          <Grid item xs={12} sm={5} className="center-and-align">
            <div
              className="paper-account-login center-and-align"
              style={{
                padding: isTabletOrMobile
                  ? "1rem 1rem 4rem 1rem"
                  : "0 4rem",
              }}
            >
              <div className="paper-account-login-div">
                <Link to="/" className="hidelink">
                  <img
                    src={logo}
                    alt="logo.png"
                    width="100px"
                    height="100px"
                    style={{ marginBottom: "1rem" }}
                  />
                </Link>

                <Typography variant="h5">Login </Typography>
                <form className="form-account" onSubmit={handleSubmit}>
                  <Grid container spacing={4}>
                    <Grid item xs={12}>
                      <TextField
                        label="Email"
                        value={email}
                        onChange={(e) => {
                          setEmail(e.target.value);
                          setErrorEmail(false);
                          setLoginError("");
                        }}
                        autoFocus
                        type="text"
                        fullWidth
                        error={errorEmail}
                        helperText={errorEmail && errorEmailMsg}
                      />
                    </Grid>
                    <Grid item xs={12}>
                      <TextField
                        label="Password"
                        fullWidth
                        value={password}
                        onChange={(e) => {
                          setPassword(e.target.value);
                          setErrorPwd(false);
                          setLoginError("");
                        }}
                        type={showPwd ? "text" : "password"}
                        error={errorPwd}
                        helperText={errorPwd && errorPwdMsg}
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
                    </Grid>
                    <Grid item xs={12} style={{ textAlign: "center" }}>
                      <Typography
                        variant="subtitle1"
                        style={{ marginTop: "-1vh" }}
                      >
                        <small style={{ color: "red" }}>{loginError}</small>
                      </Typography>

                      <div className="center-and-align">
                        <button className="btn-login" type="submit">
                          Login
                          <div className="arrow-wrapper">
                            <div className="arrow"></div>
                          </div>
                        </button>
                      </div>
                    </Grid>
                  </Grid>
                </form>
                <p>
                  Don't have an account? &nbsp;
                  <span className="link">
                    <Link to="/account/signup">Sign up</Link>
                  </span>
                </p>
                <span
                  className="link"
                  onClick={() => setOpenResetPasswordDialog(true)}
                >
                  Forgot password?
                </span>
              </div>
            </div>
          </Grid>
          <Grid
            item
            xs={0}
            md={7}
            className="login-secondary center"
            style={{ display: isTabletOrMobile ? "none" : "flex" }}
          >
            <div>
              <Typography variant="h2">Welcome on YouTorial!</Typography>
              <Typography variant="h5" style={{ marginTop: "3rem" }}>
                Start exploring the world of youtorials and share your own
                videos.
                <br></br> Learn from the best and share your knowledge with the
                world.
                <br></br>
                Teach your craft to the world, inspire and motivate students to
                learn your art.
                <br></br>
              </Typography>
            </div>
          </Grid>
        </Grid>
      </div>
      <ResetPasswordDialog
        openResetPasswordDialog={openResetPasswordDialog}
        setOpenResetPasswordDialog={setOpenResetPasswordDialog}
        setEmailSent={setEmailSent}
      />
      <Dialog
        open={emailSent}
        onClose={() => setEmailSent(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Complete your password reset</DialogTitle>
        <DialogContent>
          <DialogContentText>
            We have sent you an email with a link to reset your password.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setEmailSent(false)} className="btn-style">
            Got it!
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
