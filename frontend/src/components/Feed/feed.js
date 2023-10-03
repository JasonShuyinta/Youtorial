import React, { useState, useEffect, useContext } from "react";
import {
  Grid,
  Chip,
  Paper,
  Typography,
  Avatar,
  InputBase,
  Skeleton,
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import CloseIcon from "@mui/icons-material/Close";
import { GlobalContext } from "../../context/globalContext";
import { Link } from "react-router-dom";
import axios from "axios";
import { useMediaQuery } from "react-responsive";
import { timeConverter } from "../utils/helper";
import { category } from "../utils/constants";
import moment from "moment";
import "moment/locale/en-gb";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function Feed() {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 500px)" });

  const [searchedVideo, setSearchedVideo] = useState("");

  const context = useContext(GlobalContext);
  const { videos, searchVideo, setVideos } = context;

  useEffect(() => {
    searchVideo("");
    // eslint-disable-next-line
  }, []);

  const handleSearchByCategory = (cat) => {
    setVideos([]);
    if (cat === category[0]) searchVideo("");
    else {
      var categoryArray = [cat];
      axios
        .post(`${BASE_URL}/video/queryByCategory`, categoryArray)
        .then((res) => setVideos(res.data))
        .catch((err) => console.log(err));
    }
  };

  return (
    <>
      {isTabletOrMobile ? (
        <>
          <div className="feed-mobile">
            <Grid container>
              <Grid item xs={12}>
                <div>
                  <InputBase
                    placeholder="Search..."
                    inputProps={{ "aria-label": "search" }}
                    onChange={(e) => setSearchedVideo(e.target.value)}
                    onKeyDown={(e) => {
                      if (e.key === "Enter") {
                        searchVideo(searchedVideo);
                      }
                    }}
                    startAdornment={
                      <SearchIcon
                        onClick={() => {
                          searchVideo(searchedVideo);
                        }}
                        className="search-icon-mobile"
                      />
                    }
                    endAdornment={
                      searchedVideo !== "" && (
                        <CloseIcon
                          onClick={() => {
                            setSearchedVideo("");
                            document.getElementsByTagName("input")[0].value =
                              "";
                          }}
                        />
                      )
                    }
                    className="input-div"
                  />
                </div>
              </Grid>
              <Grid item xs={12}>
                <div className="category-container-mobile">
                  {category.map((cat) => {
                    return (
                      <Chip
                        variant="outlined"
                        onClick={() => handleSearchByCategory(cat)}
                        label={cat}
                        id={cat}
                        key={cat}
                      />
                    );
                  })}
                </div>
              </Grid>
            </Grid>
            <Grid container style={{ width: "100%" }}>
              {videos.length !== 0 ? (
                videos.map((video) => {
                  return <Video video={video} key={video.id} />;
                })
              ) : (
                <SkelMobile />
              )}
            </Grid>
          </div>
        </>
      ) : (
        <>
          <div className="feed-desktop">
            <Grid container>
              <Grid item xs={12}>
                <div>
                  <InputBase
                    placeholder="Search..."
                    inputProps={{ "aria-label": "search" }}
                    onChange={(e) => setSearchedVideo(e.target.value)}
                    onKeyDown={(e) => {
                      if (e.key === "Enter") {
                        searchVideo(searchedVideo);
                      }
                    }}
                    startAdornment={
                      <SearchIcon
                        onClick={() => {
                          searchVideo(searchedVideo);
                        }}
                        className="search-icon"
                      />
                    }
                    endAdornment={
                      searchedVideo !== "" && (
                        <CloseIcon
                          onClick={() => {
                            setSearchedVideo("");
                            document.getElementsByTagName("input")[0].value =
                              "";
                          }}
                          className="search-icon"
                        />
                      )
                    }
                    className="input-div"
                  />
                </div>
              </Grid>
              <Grid item xs={12}>
                <div className="feedChips">
                  {category.map((cat) => {
                    return (
                      <Chip
                        variant="outlined"
                        onClick={() => handleSearchByCategory(cat)}
                        label={cat}
                        id={cat}
                        key={cat}
                      />
                    );
                  })}
                </div>
              </Grid>
            </Grid>
            <div className="feed-video-container">
              <Grid container spacing={1}>
                {videos.length !== 0 ? (
                  videos.map((video) => {
                    return <Video video={video} key={video.id} />;
                  })
                ) : (
                  <Skel />
                )}
              </Grid>
            </div>
          </div>
        </>
      )}
    </>
  );
}

function Video({ video }) {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 500px)" });

  const [user, setUser] = useState("");
  const [duration, setDuration] = useState("");

  useEffect(() => {
    if (video !== "" && video !== undefined) {
      setDuration(timeConverter(video.duration));
      axios
        .get(`${BASE_URL}/user/${video?.author}`)
        .then((res) => {
          setUser(res.data);
        })
        .catch((err) => console.log(err));
    }
  }, [video]);

  return (
    <>
      {isTabletOrMobile ? (
        <Grid item xs={12} className="center pb-4">
          <div>
            <Link to={{ pathname: `/learn/${video.id}` }}>
              <Paper
                elevation={3}
                className="hvr-underline-from-center paper-grid-mobile"
              >
                <div className="img-container-mobile">
                  <img loading="lazy" src={video.thumbnail} alt="thumbnail" />
                </div>
                <div
                  className="info-container-mobile"
                  style={{ padding: "3vw" }}
                >
                  <Typography variant="h6" className="video-title">
                    {video.title}
                  </Typography>
                  <div className="author-container">
                    <Avatar src={user?.image} alt="avatar" />
                    <Typography variant="subtitle1">
                      {user?.username}
                    </Typography>
                  </div>
                  <Typography variant="subtitle1">{duration}</Typography>
                  <div className="category-container">
                    {video?.category.length !== 0 &&
                      video?.category.map((cat) => {
                        return (
                          <span key={cat} style={{ marginRight: "0.3rem" }}>
                            #{cat} &nbsp;
                          </span>
                        );
                      })}
                  </div>
                  <div>
                    <Typography variant="body2">
                      Uploaded{" "}
                      <span className="timeagoStyle">
                        {moment(video?.uploadDate).fromNow()}
                      </span>
                    </Typography>
                  </div>
                </div>
              </Paper>
            </Link>
          </div>
        </Grid>
      ) : (
        <Grid item xs={12} sm={6} md={3} className="feed-video-grid">
          <Link to={{ pathname: `/learn/${video.id}` }}>
            <Paper
              elevation={3}
              className="hvr-underline-from-center paper-grid"
            >
              <div className="img-container">
                <img src={video.thumbnail} alt="thumbnail" />
              </div>
              <div className="info-container">
                <Typography variant="h6" className="video-title">
                  {video.title}
                </Typography>
                <div className="author-container">
                  <Avatar src={user?.image} alt="avatar" />
                  <Typography variant="subtitle1">{user?.username}</Typography>
                </div>
                <div className="category-container">
                  {video?.category?.length !== 0 &&
                    video?.category.map((cat) => {
                      return <span key={cat}>#{cat} &nbsp;</span>;
                    })}
                </div>
                <Typography variant="subtitle1">{duration}</Typography>
                <div>
                  <Typography variant="body2">
                    Uploaded{" "}
                    <span className="timeagoStyle">
                      {moment(video?.uploadDate).fromNow()}
                    </span>
                  </Typography>
                </div>
              </div>
            </Paper>
          </Link>
        </Grid>
      )}
    </>
  );
}

function SkelMobile() {
  var skeletonNum = [];
  for (let i = 0; i < 20; i++) {
    skeletonNum.push(
      <Grid item xs={12} key={i} className="center">
        <Skeleton variant="rect" style={{ width: "80%", height: "200px" }} />
      </Grid>
    );
  }
  return <>{skeletonNum}</>;
}

function Skel() {
  var skeletonNum = [];
  for (let i = 0; i < 20; i++) {
    skeletonNum.push(
      <Grid item xs={12} sm={6} md={3} className="feed-video-grid" key={i}>
        <Skeleton variant="rect" className="feed-skel" />
      </Grid>
    );
  }
  return <>{skeletonNum}</>;
}
