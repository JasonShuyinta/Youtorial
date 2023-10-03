import React, { useState, createContext, useEffect } from "react";
import axios from "axios";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export const GlobalContext = createContext();

export const GlobalContextProvider = (props) => {
  const [videos, setVideos] = useState([]);
  const [video, setVideo] = useState("");
  const [selectedTutor, setSelectedTutor] = useState(null);
  const [activeStep, setActiveStep] = useState(0);
  const [particularActiveStep, setParticularActiveStep] = useState(0);
  const [stats, setStats] = useState(false);
  const [showSnack, setShowSnack] = useState(false);
  const [snackMsg, setSnackMsg] = useState("");
  const [config, setConfig] = useState(
    localStorage.getItem("accesstoken")
      ? {
          headers: {
            authorization: `Bearer ${localStorage.getItem("accesstoken")}`,
          },
        }
      : null
  );

  const [published, setPublished] = useState(false);
  const [isSame, setIsSame] = useState(true);
  const [markerId, setMarkerId] = useState("");
  const [loggedUser, setLoggedUser] = useState("");
  const [loggedUserIsAuthor, setLoggedUserIsAuthor] = useState(false);
  const [tutorSub, setTutorSub] = useState(null);
  const [confirmationEmail, setConfirmationEmail] = useState("");
  const [signedUpUserId, setSignedUpUserId] = useState("");
  const [successfulUpdate, setSuccessfulUpdate] = useState(false);


  const [editVideoItem, setEditVideoItem] = useState("");

  useEffect(() => {
    if (config) {
      axios
        .get(`${BASE_URL}/user/getUserByToken`, config)
        .then((res) => {
          if (res.status === 200) setLoggedUser(res.data);
          else {
            setLoggedUser(null);
            if(res.status === 205) console.log("Token is invalid");
            if(res.status === 204) console.log("Token is expired");
          }
        })
        .catch((err) => {
          console.log(err.response.status);
        });
    }
  }, [config]);

  const getHomeVideos = () => {
    axios
      .get(`${BASE_URL}/video`)
      .then((res) => {
        setVideos(res.data);
      })
      .catch((err) => console.log(err));
  };

  const searchVideo = (searchInput) => {
    if (searchInput !== "") {
      axios
        .post(`${BASE_URL}/video/searchVideo`, { searchInput })
        .then((res) => setVideos(res.data))
        .catch((err) => console.log(err));
    } else getHomeVideos();
  };

  const checkDifferences = (tutorialId) => {
    axios
      .get(`${BASE_URL}/tutorial/checkDifferences/${tutorialId}`, config)
      .then((res) => setIsSame(res.data))
      .catch((err) => console.log(err));
  };

  return (
    <GlobalContext.Provider
      value={{
        config,
        setConfig,
        videos,
        setVideos,
        video,
        setVideo,
        selectedTutor,
        setSelectedTutor,
        activeStep,
        setActiveStep,
        particularActiveStep,
        setParticularActiveStep,
        stats,
        setStats,
        showSnack,
        setShowSnack,
        snackMsg,
        setSnackMsg,
        published,
        setPublished,
        isSame,
        setIsSame,
        markerId,
        setMarkerId,
        searchVideo,
        checkDifferences,
        loggedUser,
        setLoggedUser,
        loggedUserIsAuthor,
        setLoggedUserIsAuthor,
        tutorSub,
        setTutorSub,
        confirmationEmail,
        setConfirmationEmail,
        signedUpUserId,
        setSignedUpUserId,
        editVideoItem, setEditVideoItem,
        successfulUpdate, setSuccessfulUpdate
      }}
    >
      {props.children}
    </GlobalContext.Provider>
  );
};
