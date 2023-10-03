import React, { useContext, useState } from "react";
import {
  Grid,
  Typography,
  TextField,
  Avatar,
  InputAdornment,
  Checkbox,
  FormControl,
  FormControlLabel,
} from "@mui/material";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import axios from "axios";
import { GlobalContext } from "../../context/globalContext";
import { useHistory, Link } from "react-router-dom";
import { logo } from "../utils/constants";
import Loading from "../Learn/LearnTutorial/Utils/loading";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function Signup() {
  const [showPwd, setShowPwd] = useState(false);
  const [characterCounter, setCharacterCounter] = useState(0);
  const [image, setImage] = useState("");
  const [signupError, setSignupError] = useState("");
  const [loading, setLoading] = useState(false);

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [terms, setTerms] = useState(false);

  const [errorUsername, setErrorUsername] = useState(false);
  const [errorEmail, setErrorEmail] = useState(false);
  const [errorPwd, setErrorPwd] = useState(false);
  const [errorUsernameMsg, setErrorUsernameMsg] = useState("Error");
  const [errorEmailMsg, setErrorEmailMsg] = useState("Error");
  const [errorPwdMsg, setErrorPwdMsg] = useState("Error");

  const history = useHistory();

  const { setConfirmationEmail, setSignedUpUserId } = useContext(GlobalContext);

  const charCounter = (e) => {
    setCharacterCounter(e.target.value.length);
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    var submitForm = true;
    const regexExp =
      /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/gi;

    if (firstName === "" || lastName === "") {
      submitForm = false;
    }

    const usernameRegex = /^[a-zA-Z0-9]+$/;
    if (!usernameRegex.test(username)) {
      setErrorUsername(true);
      setErrorUsernameMsg("Username can only contain letters and numbers");
      submitForm = false;
    }

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
      setLoading(true);

      axios
        .post(`${BASE_URL}/user/signup`, {
          email: email.toLowerCase(),
          password,
          firstName,
          lastName,
          username,
          image,
        })
        .then((res) => {
          if (res.status === 200) {
            setLoading(false);
            setConfirmationEmail(email.toLowerCase());
            setSignedUpUserId(res.data.id);
            history.push("/account/verify");
          }
        })
        .catch((err) => {
          //Handle errors
          setLoading(false);
          if (err.response.status === 400)
            setSignupError("Username already present");
          else if (err.response.status === 409)
            setSignupError("This email is already registered");
          else setSignupError("Server Error, try again later");
        });
    }
  };

  const handleAvatarFile = (e) => {
    var file = e.target.files[0];
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function () {
      setImage(reader.result);
    };
    reader.onerror = function (error) {
      console.log("Error: ", error);
    };
  };

  return (
    <div className="signup-container">
      <Grid container>
        <Grid item xs={0} sm={5}></Grid>
        <Grid item xs={12} sm={7} className="signup-form-container">
          <div className="paper-account">
            <Link to="/" className="hidelink">
              <img
                src={logo}
                alt="logo.png"
                width="100px"
                height="100px"
                style={{ marginBottom: "1rem" }}
              />
            </Link>
            <Avatar src={image} alt="image" />
            <Typography variant="h4">Sign up </Typography>
            <form className="form-account" onSubmit={handleSubmit}>
              <Grid container spacing={4}>
                <Grid item xs={12}>
                  <TextField
                    label="First name"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                    autoFocus
                    fullWidth
                    required
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    label="Last name"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    fullWidth
                    required
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    label="Username"
                    value={username}
                    onChange={(e) => {
                      setUsername(e.target.value);
                      setSignupError("");
                      setErrorUsername(false);
                      setErrorUsernameMsg("");
                    }}
                    error={errorUsername}
                    helperText={errorUsername && errorUsernameMsg}
                    fullWidth
                    required
                    onKeyUp={charCounter}
                    inputprop={{ maxLength: "15" }}
                    InputProps={{
                      endAdornment: (
                        <InputAdornment
                          position="end"
                          style={{ color: "gray" }}
                        >
                          {characterCounter}/15
                        </InputAdornment>
                      ),
                    }}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    label="Email"
                    value={email}
                    onChange={(e) => {
                      setEmail(e.target.value);
                      setErrorEmail(false);
                      setSignupError("");
                    }}
                    autoFocus
                    type="text"
                    fullWidth
                    required
                    error={errorEmail}
                    helperText={errorEmail && errorEmailMsg}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    label="Password"
                    fullWidth
                    required
                    value={password}
                    onChange={(e) => {
                      setPassword(e.target.value);
                      setErrorPwd(false);
                      setSignupError("");
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
                <Grid item xs={12}>
                  <p>
                    <b>Choose an avatar</b>
                  </p>
                  <div className="fileInput">
                    <input
                      type="file"
                      multiple={false}
                      id="avatar-file"
                      accept="image/png, image/jpeg"
                      onChange={(e) => handleAvatarFile(e)}
                    />
                  </div>
                </Grid>
                <Grid item x={12} className="terms">
                  <FormControl component="fieldset" variant="standard">
                    <FormControlLabel
                      control={
                        <Checkbox
                          required
                          checked={terms}
                          onChange={(e) => {
                            setTerms(e.target.checked);
                          }}
                          inputProps={{ "aria-label": "controlled" }}
                        />
                      }
                      label=""
                    />
                  </FormControl>
                  <Typography variant="body2">
                    <b>
                      I accept the{" "}
                      <span className="link">
                        <Link to="/privacy-policy" target="_blank">
                          Privacy Policy
                        </Link>
                      </span>
                      &nbsp;and the&nbsp;
                      <span className="link">
                        <Link to="/terms-and-conditions" target="_blank">
                          Terms of Service
                        </Link>
                      </span>
                      .
                    </b>
                  </Typography>
                </Grid>
                <Grid
                  item
                  xs={12}
                  style={{ display: loading ? "block" : "none" }}
                >
                  {loading && <Loading />}
                </Grid>
                <Grid item xs={12}>
                  <div className="center-and-align">
                    <Typography>
                      <small className="account-error">{signupError}</small>
                    </Typography>
                  </div>
                  <div className="center-and-align">
                    <button className="btn-login" type="submit">
                      Sign up
                      <div className="arrow-wrapper">
                        <div className="arrow"></div>
                      </div>
                    </button>
                  </div>
                </Grid>
              </Grid>
            </form>
            <p>
              Already have an account? &nbsp;
              <Link to="/account/login">Login</Link>
            </p>
          </div>
        </Grid>
      </Grid>
    </div>
  );
}
