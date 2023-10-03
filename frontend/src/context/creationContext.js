import React, { useState, useContext, createContext } from "react";
import { standard, xml } from "../components/utils/constants";
import { v4 as uuidv4 } from "uuid";
import axios from "axios";
import { GlobalContext } from "./globalContext";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export const CreationContext = createContext();

export const CreationProvider = (props) => {
  const context = useContext(GlobalContext);
  const {
    video,
    setActiveStep,
    config,
    setShowSnack,
    setSnackMsg,
    loggedUserIsAuthor,
    setParticularActiveStep,
  } = context;

  const [title, setTitle] = useState("");
  const [instruction, setInstruction] = useState("");
  const [type, setType] = useState(standard);

  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");
  const [requisites, setRequisites] = useState([]);
  //step metadata
  const [timeError, setTimeError] = useState(false);
  const [overTimeError, setOverTimeError] = useState(false);
  const [fullScreen, setFullScreen] = useState(false);
  const [updateMode, setUpdateMode] = useState(false);
  const [stepIdMod, setStepIdMod] = useState("");

  //coding
  const [code, setCode] = useState("");
  const [language, setLanguage] = useState(xml);

  //cooking
  const [ingredients, setIngredients] = useState([
    { id: uuidv4(), quantity: "", ingredient: "" },
  ]);
  const [cookingImage, setCookingImage] = useState("");
  const [numPeople, setNumPeople] = useState(1);
  const [selectedImageUrl, setSelectedImageUrl] = React.useState(null);

  //education
  const [formula, setFormula] = useState("");

  //music
  const [audioFile, setAudioFile] = useState("");
  const [audioName, setAudioName] = useState("");
  const [inputKey, setInputKey] = useState("");

  //custom
  const [childrenProp, setChildrenProp] = useState([]);

  const handleAddLink = ({ obj }) => {
    setRequisites([...requisites, { name: obj.name, link: obj.link }]);
  };
  const handleRemoveLink = (obj) => {
    let tmpArray = requisites.filter((item) => item !== obj);
    setRequisites(tmpArray);
  };
  const handleSuccess = (data) => {
    setInstruction("");
    setCode("");
    setShowSnack(true);
    setSnackMsg("Step saved!");
    setRequisites([]);
    updateTitle();
    setCode("");
    setFormula("");
    setAudioFile("");
    setAudioName("");
    setCookingImage("");
    setSelectedImageUrl(null);
    setInputKey(Math.random().toString(36));
    setStartTime(data.steps[data.steps.length - 1].endTime);
    document.getElementById("image-upload").value = "";
  };

  const handleUpdateSuccess = () => {
    setActiveStep(1);
    setShowSnack(true);
    setSnackMsg("Step updated!");
    setUpdateMode(false);
    setInstruction("");
    setRequisites([]);
    setCode("");
    setFormula("");
    setSelectedImageUrl(null);
    setInputKey(Math.random().toString(36));
    setLanguage(xml);
    setIngredients([{ id: uuidv4(), quantity: "", ingredient: "" }]);
    setCookingImage("");
    setNumPeople(1);
    setFormula("");
    setAudioName("");
    setAudioFile("");
    setInputKey("");
  };

  const updateTitle = () => {
    axios
      .get(`${BASE_URL}/tutorial/getStepsNumber/${video.id}`)
      .then((res) => setTitle(`Step ${res.data}`))
      .catch((err) => console.log(err));
  };

  const handleModify = (stepId) => {
    axios
      .get(`${BASE_URL}/step/${stepId}`)
      .then((res) => {
        if (res.status === 200) {
          setUpdateMode(true);
          setStepIdMod(stepId);
          if (video?.lock && loggedUserIsAuthor) setParticularActiveStep(1);
          else setActiveStep(0);
        }
      })
      .catch((err) => console.log(err));
  };

  const handleSubmit = () => {
    const videoDurationFormatted = new Date(Math.trunc(video.duration))
      .toISOString()
      .slice(11, 19);

    if (title === "") {
      alert("You must fill the required field!");
    } else {
      if (startTime >= endTime) {
        setTimeError(true);
      } else {
        setTimeError(false);
        if (endTime > videoDurationFormatted) {
          setOverTimeError(true);
        } else {
          setOverTimeError(false);
          if (updateMode) {
            axios
              .put(
                `${BASE_URL}/tutorial/updateStep/${stepIdMod}`,
                {
                  title,
                  instruction,
                  endTime,
                  startTime,
                  links: requisites,
                  videoId: video.id,
                  image: cookingImage,
                  //coding
                  language,
                  code,
                  //education
                  formula,
                  //music
                  audioFile,
                  audioName,
                },
                config
              )
              .then((res) => {
                if (res.status === 200) handleUpdateSuccess();
              })
              .catch((err) => console.log(err));
          } else {
            
            axios
              .post(
                `${BASE_URL}/tutorial/saveStep`,
                {
                  title,
                  instruction,
                  endTime,
                  startTime,
                  links: requisites,
                  videoId: video.id,
                  image: cookingImage,
                  //coding
                  language,
                  code,
                  //education
                  formula,
                  //music
                  audioFile,
                  audioName,
                },
                config
              )
              .then((res) => {
                handleSuccess(res.data);
              })
              .catch((err) => console.log(err));
          }
        }
      }
    }
  };

  return (
    <CreationContext.Provider
      value={{
        title,
        setTitle,
        instruction,
        setInstruction,
        type,
        setType,
        startTime,
        setStartTime,
        endTime,
        setEndTime,
        requisites,
        setRequisites,
        //coding
        code,
        setCode,
        language,
        setLanguage,
        //cooking
        ingredients,
        setIngredients,
        cookingImage,
        setCookingImage,
        numPeople,
        setNumPeople,
        //education
        formula,
        setFormula,
        //music
        audioFile,
        setAudioFile,
        audioName,
        setAudioName,
        inputKey,
        setInputKey,
        childrenProp,
        setChildrenProp,
        //utils
        timeError,
        setTimeError,
        overTimeError,
        setOverTimeError,
        fullScreen,
        setFullScreen,
        updateMode,
        setUpdateMode,
        selectedImageUrl,
        setSelectedImageUrl,
        updateTitle,
        handleAddLink,
        handleRemoveLink,
        handleModify,
        handleSubmit,
        stepIdMod,
      }}
    >
      {props.children}
    </CreationContext.Provider>
  );
};
