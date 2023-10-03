import {
  Box,
  Button,
  Container,
  Paper,
  Typography,
  TextField,
  Rating,
} from "@mui/material";
import React, { useState, useContext } from "react";
import SentimentVeryDissatisfiedIcon from "@mui/icons-material/SentimentVeryDissatisfied";
import SentimentDissatisfiedIcon from "@mui/icons-material/SentimentDissatisfied";
import SentimentSatisfiedIcon from "@mui/icons-material/SentimentSatisfied";
import SentimentSatisfiedAltIcon from "@mui/icons-material/SentimentSatisfiedAltOutlined";
import SentimentVerySatisfiedIcon from "@mui/icons-material/SentimentVerySatisfied";
import SendIcon from "@mui/icons-material/Send";
import PropTypes from "prop-types";
import { styled } from "@mui/material/styles";
import DoneIcon from "@mui/icons-material/Done";
import axios from "axios";
import { GlobalContext } from "../../context/globalContext";
import { Link } from "react-router-dom";
import { useMediaQuery } from "react-responsive";

const StyledRating = styled(Rating)(({ theme }) => ({
  "& .MuiRating-iconEmpty .MuiSvgIcon-root": {
    color: theme.palette.action.disabled,
  },
}));

const customIcons = {
  1: {
    icon: <SentimentVeryDissatisfiedIcon color="error" />,
    label: "Very Dissatisfied",
  },
  2: {
    icon: <SentimentDissatisfiedIcon color="error" />,
    label: "Dissatisfied",
  },
  3: {
    icon: <SentimentSatisfiedIcon color="warning" />,
    label: "Neutral",
  },
  4: {
    icon: <SentimentSatisfiedAltIcon color="success" />,
    label: "Satisfied",
  },
  5: {
    icon: <SentimentVerySatisfiedIcon color="success" />,
    label: "Very Satisfied",
  },
};

function IconContainer(props) {
  const { value, ...other } = props;
  return <span {...other}>{customIcons[value].icon}</span>;
}

IconContainer.propTypes = {
  value: PropTypes.number.isRequired,
};

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function Feedback() {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });

  const [goalAchieved, setGoalAchieved] = useState("");
  const [satisfaction, setSatisfaction] = useState(2);
  const [suggestions, setSuggestions] = useState("");
  const [feedbackSubmitted, setFeedbackSubmitted] = useState(false);

  const context = useContext(GlobalContext);
  const { config } = context;

  const handleSubmit = (e) => {
    axios
      .post(
        `${BASE_URL}/user/user-feedback`,
        {
          goalAchieved,
          satisfaction,
          suggestions,
        },
        config
      )
      .then((res) => {
        if (res.status === 200) {
          if (res.data) {
            setFeedbackSubmitted(true);
          }
        }
      })
      .catch((err) => console.log(err));
  };

  const handleSuggestions = (e) => {
    setSuggestions(e.target.value);
  };

  return (
    <div className="full-height center-and-align">
      <Container className={isTabletOrMobile ? `w-100` : `w-50`}>
        <Box>
          <Paper elevation={3}>
            <div
              className="center-and-start p-1"
              style={{
                backgroundColor: "#7d88ea",
                height: "4rem",
              }}
            >
              <h1>Send us your feedback</h1>
            </div>
            {feedbackSubmitted ? (
              <div className="p-1">
                <div className="center-and-align" style={{ height: "30vh" }}>
                  <div>
                    <Typography variant="h5">
                      Thank you for sending us your feedback!
                    </Typography>
                    <div className="center-and-align mt-2">
                      <Button variant="contained" className="btn-style">
                        <Link to="/" className="hidelink">
                          Go back to home
                        </Link>
                      </Button>
                    </div>
                  </div>
                </div>
              </div>
            ) : (
              <div className="p-1">
                <div className="text-center mtb-2">
                  <Typography variant="h6">
                    Did you achieve your goal?
                  </Typography>
                  <div className="center-and-align mtb-1">
                    <Button
                      variant="contained"
                      className="mr-1 btn-style"
                      startIcon={goalAchieved === "Yes" && <DoneIcon />}
                      onClick={() => setGoalAchieved("Yes")}
                    >
                      Yes
                    </Button>
                    <Button
                      variant="contained"
                      className="mr-1 btn-style"
                      startIcon={goalAchieved === "Partially" && <DoneIcon />}
                      onClick={() => setGoalAchieved("Partially")}
                    >
                      Partially
                    </Button>
                    <Button
                      variant="contained"
                      className="btn-style"
                      startIcon={goalAchieved === "No" && <DoneIcon />}
                      onClick={() => setGoalAchieved("No")}
                    >
                      No
                    </Button>
                  </div>
                </div>
                <div className="text-center mtb-2">
                  <Typography variant="h6">
                    How would you rate YouTorial?
                  </Typography>
                  <div className="center-and-align mtb-1">
                    <StyledRating
                      defaultValue={satisfaction}
                      IconContainerComponent={IconContainer}
                      getLabelText={(value) => customIcons[value].label}
                      highlightSelectedOnly
                      onChange={(event, newValue) => {
                        setSatisfaction(newValue);
                      }}
                    />
                  </div>
                </div>
                <div className="text-center mtb-2">
                  <Typography variant="h6">
                    Do you have any suggestions to make our website better?
                  </Typography>
                  <TextField
                    label="Suggestions..."
                    multiline
                    minRows={6}
                    maxRows={6}
                    value={suggestions}
                    onChange={handleSuggestions}
                    className={`mtb-1 ${isTabletOrMobile ? `w-100` : `w-50`}`}
                  />
                </div>
                <div className="center-and-end">
                  <Button
                    variant="contained"
                    className="btn-style"
                    endIcon={<SendIcon />}
                    onClick={() => handleSubmit()}
                  >
                    Submit
                  </Button>
                </div>
              </div>
            )}
          </Paper>
        </Box>
      </Container>
    </div>
  );
}
