import { Avatar, Grid, Paper, Typography } from "@mui/material";
import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { GlobalContext } from "../../context/globalContext";
import { Link } from "react-router-dom";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function Channels() {
  const [tutors, setTutors] = useState([]);
  const context = useContext(GlobalContext);
  const { loggedUser, config } = context;

  useEffect(() => {
    if (loggedUser !== null && config !== null)
      axios
        .get(`${BASE_URL}/interaction/getSubscriptions`, config)
        .then((res) => setTutors(res.data))
        .catch((err) => console.log(err));
  }, [loggedUser, config]);

  return (
    <div className="min-height-container">
      {tutors.length > 0 ? (
        <>
          <div className="channel-container">
            <Typography variant="h6">Your favourite tutors</Typography>
          </div>
          <Grid container className="subscription-container">
            {tutors.map((tutor) => {
              return <Tutor key={tutor} tutor={tutor} />;
            })}
          </Grid>
        </>
      ) : (
        <div className="channel-container" style={{ height: "100vh" }}>
          <Typography variant="h5">
            You have not subscribed to any channel yet!
          </Typography>
        </div>
      )}
    </div>
  );
}

function Tutor({ tutor }) {
  const [user, setUser] = useState(null);

  const context = React.useContext(GlobalContext);
  const { setTutorSub } = context;

  useEffect(() => {
    axios
      .get(`${BASE_URL}/user/${tutor}`)
      .then((res) => setUser(res.data))
      .catch((err) => console.log(err));
  }, [tutor]);

  return (
    <Grid item xs={4}>
      <Link
        to={{ pathname: `/tutor/${user?.username}` }}
        className="hidelink"
        onClick={() => setTutorSub(user)}
      >
        <Paper className="tutor-info" elevation={3}>
          <Avatar src={user?.image} alt={user?.image} />
          <div className="center-and-align">
            <Typography variant="subtitle1" className="channel-username">
              {" "}
              {user?.username}
            </Typography>
          </div>
        </Paper>
      </Link>
    </Grid>
  );
}
