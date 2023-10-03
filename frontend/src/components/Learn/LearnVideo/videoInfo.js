import React, { useState, useEffect, useContext } from "react";
import {
  Typography,
  Avatar,
  Button,
  Skeleton,
  Grid,
  Divider,
  Paper,
  Collapse,
} from "@mui/material";
import moment from "moment";
import "moment/locale/en-gb";
import axios from "axios";
import { GlobalContext } from "../../../context/globalContext";
import { useMediaQuery } from "react-responsive";
import parse from "html-react-parser";
import Linkify from "react-linkify";
import { FaLock, FaLockOpen } from "react-icons/fa";
import { GrDown, GrUp } from "react-icons/gr";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function VideoInfo({ video }) {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });

  const [user, setUser] = useState("");
  const [subscribed, setSubscribed] = useState(false);
  const [isAuthor, setIsAuthor] = useState(false);
  const [reduceVideoInfo, setReduceVideoInfo] = useState(false);

  const context = useContext(GlobalContext);
  const { config, loggedUser } = context;

  useEffect(() => {
    if (video !== "" && video !== undefined && video !== null) {
      axios
        .get(`${BASE_URL}/user/${video.author}`)
        .then((res) => setUser(res.data));

      if (loggedUser) {
        axios
          .get(`${BASE_URL}/interaction/isSubscribed/${video.author}`, config)
          .then((res) => {
            setSubscribed(res.data);
          })
          .catch((error) => console.error(error));
        if (loggedUser.id === video.author) setIsAuthor(true);
        else setIsAuthor(false);
      } else setIsAuthor(false);
    }
  }, [video, config, loggedUser]);

  const handleSubscription = (subscribed) => {
    if (subscribed) {
      axios
        .get(`${BASE_URL}/interaction/unsubscribe/${video.author}`, config)
        .then((res) => {
          if (res.status === 200) setSubscribed(false);
        })
        .catch((error) => console.error(error));
    } else {
      axios
        .get(`${BASE_URL}/interaction/subscribe/${video.author}`, config)
        .then((res) => {
          if (res.status === 200) setSubscribed(true);
        })
        .catch((error) => console.error(error));
    }
  };

  return (
    <>
      {isTabletOrMobile ? (
        <div style={{ width: "100%" }}>
          <Paper style={{ padding: "0.5rem" }}>
            <div className="watchVideoInfo">
              {video ? (
                <>
                  <div className="center-and-end mr-1">
                    {reduceVideoInfo ? (
                      <GrUp onClick={() => setReduceVideoInfo(false)} />
                    ) : (
                      <GrDown onClick={() => setReduceVideoInfo(true)} />
                    )}
                  </div>
                  <Collapse in={reduceVideoInfo}>
                    <Grid
                      container
                      className="disp-flex"
                      style={{ marginTop: "0.25rem" }}
                    >
                      <Grid item xs={12}>
                        <h2>{video.title}</h2>
                        <div className="watchVideoInfo2">
                          <Avatar src={user?.image} alt="Avatar" />
                          <Typography variant="subtitle1">
                            {user?.username}{" "}
                          </Typography>
                          {!isAuthor && (
                            <Button
                              className="sub-btn"
                              onClick={() => handleSubscription()}
                            >
                              {" "}
                              {subscribed ? "Subscribed" : "Subscribe now"}
                            </Button>
                          )}
                        </div>
                        <div>
                          <Grid container className="video-mobile-publish-info">
                            <Grid item xs={6}>
                              Published{" "}
                              <span> {moment(video.uploadDate).fromNow()}</span>
                            </Grid>
                            <Grid item xs={6}>
                              <span>
                                {video.lock ? <FaLock /> : <FaLockOpen />}
                              </span>
                            </Grid>
                          </Grid>
                        </div>

                        <div className="watchVideoInfo3">
                          {video !== "" &&
                            video.category !== undefined &&
                            video?.category.length > 0 &&
                            video?.category.map((cat) => {
                              return <p key={cat}>#{cat} &nbsp; </p>;
                            })}
                        </div>
                      </Grid>
                    </Grid>
                    <Divider style={{ margin: "1rem 0 0 -1rem" }} />
                    <div className="video-description">
                      {/* <Linkify>{ReactHtmlParser(video.description)}</Linkify> */}
                      <Linkify>{parse(video.description)}</Linkify>
                    </div>
                  </Collapse>
                </>
              ) : (
                <div style={{ marginTop: "3rem", display: "block" }}>
                  <Skeleton variant="text" width={600} height={50} />
                  <div style={{ display: "flex" }}>
                    <Skeleton
                      variant="circle"
                      width={50}
                      height={50}
                      style={{ marginRight: "1rem" }}
                    />
                    <Skeleton variant="text" width={300} height={50} />
                  </div>
                  <Skeleton variant="text" width={300} height={50} />
                  <Skeleton variant="text" width={300} height={50} />
                </div>
              )}
            </div>
          </Paper>
        </div>
      ) : (
        <>
          <div className="video-info-desktop">
            <div>
              <Typography variant="subtitle1">{video?.title}</Typography>
            </div>
            <div style={{ display: "flex" }}>
              <div className="center-and-align">
                {video !== "" &&
                  video.category !== undefined &&
                  video.category !== null &&
                  video.category.length > 0 &&
                  video.category.map((cat) => {
                    return (
                      <p className="video-category-paragraph" key={cat}>
                        #{cat}
                      </p>
                    );
                  })}
              </div>
              <div className="publish-time">
                <Typography variant="subtitle1">
                  Published{" "}
                  <span>{moment(video?.uploadDate).fromNow()} &nbsp;</span>
                </Typography>
              </div>
            </div>
          </div>
          <div className="video-info-secondary">
            <div>
              <Avatar src={user?.image} alt="Avatar" />
              <Typography variant="subtitle1">{user?.username} </Typography>
            </div>
            <div style={{ flexGrow: "1" }}></div>
            {loggedUser && (
              <div>
                {!isAuthor && (
                  <Button
                    className="sub-btn btn-style"
                    onClick={() => handleSubscription(subscribed)}
                  >
                    {" "}
                    {subscribed ? "Subscribed!" : "Subscribe now"}
                  </Button>
                )}
              </div>
            )}
          </div>
        </>
      )}
    </>
  );
}
