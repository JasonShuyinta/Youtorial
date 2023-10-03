import React, { useContext, useState, useEffect } from "react";
import {
  Grid,
  TextField,
  InputAdornment,
  Input,
  Paper,
  Tab,
  Tabs,
} from "@mui/material";
import Time from "./fullScreenUtils/time";
import Links from "./fullScreenUtils/links";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import 'katex/dist/katex.min.css';
import TeX from '@matejmazur/react-katex';
import { CreationContext } from "../../../../context/creationContext";
import { GlobalContext } from "../../../../context/globalContext";
import {
  formats,
  xml,
  javascript,
  python,
  php,
  modules,
} from "../../../utils/constants";
import Editor from "@monaco-editor/react";
import MusicNoteSharpIcon from "@mui/icons-material/MusicNoteSharp";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import { useMediaQuery } from "react-responsive";
import axios from "axios";
import CloseIcon from "@mui/icons-material/Close";
import moment from "moment";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function CreateStep({ updateMode }) {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });
  const create = useContext(CreationContext);
  const {
    title,
    setTitle,
    instruction,
    setCookingImage,
    selectedImageUrl,
    setSelectedImageUrl,
    setInstruction,
    formula,
    setFormula,
    setAudioFile,
    audioName,
    setAudioName,
    inputKey,
    code,
    setCode,
    language,
    setLanguage,
    handleRemoveLink,
    handleAddLink,
    stepIdMod,
    setStartTime,
    setEndTime,
    setRequisites,
  } = create;

  const context = useContext(GlobalContext);
  const { video, config } = context;

  const [selectedImage, setSelectedImage] = useState(null);
  const [val, setVal] = useState(0);
  const [visibilityMath, setVisibilityMath] = useState(false);
  const [visibilityCode, setVisibilityCode] = useState(false);
  const [visibilityAudio, setVisibilityAudio] = useState(false);
  const [hoverImage, setHoverImage] = useState(false);

  useEffect(() => {
    if (video) {
      if (updateMode) {
        axios
          .get(`${BASE_URL}/step/${stepIdMod}`)
          .then((res) => {
            setTitle(res.data.title);
            setInstruction(res.data.instruction);
            setRequisites(res.data.links);
            setStartTime(res.data.startTime);
            handleImageRendering(res.data.image);
            //code
            setCode(res.data.code);
            setLanguage(res.data.language);
            //maths
            setFormula(res.data.formula);
            //music
            setAudioName(res.data.audioName);
            setAudioFile(res.data.audioFile);
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        axios
          .get(`${BASE_URL}/tutorial/getSelectedTutorial/${video.id}`, config)
          .then((res) => {
            if (res.status === 204) {
              setStartTime("00:00:00");
              setTitle("Step 0");
            } else if (res.status === 200) {
              if (res.data.steps != null && res.data.steps.length === 0) {
                setStartTime("00:00:00");
                setTitle("Step 0");
              } else {
                var stepsArray = res.data.steps;
                var lastStep = stepsArray[stepsArray.length - 1];
                setTitle(`Step ${stepsArray.length}`);
                setInstruction("");
                setRequisites([]);
                setStartTime(lastStep.endTime);
                //Adding one second to startTime
                setEndTime(
                  moment
                    .utc(
                      (moment.duration(lastStep.endTime).asSeconds() + 1) * 1000
                    )
                    .format("HH:mm:ss")
                );
                setSelectedImageUrl("");
                //code
                setCode("");
                setLanguage(xml);
                //maths
                setFormula("");
                //music
                setAudioName("");
                setAudioFile("");
              }
            }
          })
          .catch((err) => console.log(err));
      }
    }
    // eslint-disable-next-line
  }, [video, updateMode, stepIdMod]);

  function handleImageRendering(imgBase64) {
    var image = new Image();
    image.src = imgBase64;
    setSelectedImageUrl(imgBase64);
    return image;
  }

  const handleImageFile = (e) => {
    var file = e.target.files[0];
    setSelectedImage(file);
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function () {
      setCookingImage(reader.result);
    };
    reader.onerror = function (error) {
      console.log("Error: ", error);
    };
  };

  const audioToBase64 = (e) => {
    if(e.target.files[0].size > 16*1024*1024) {
      alert("File is too big!");
      document.getElementById("fileAudio").value="";
    } else {
      return new Promise((resolve, reject) => {
        let reader = new FileReader();
        reader.onerror = reject;
        reader.readAsDataURL(e.target.files[0]);
        reader.onload = function (e) {
          setAudioFile(reader.result);
        };
      });
    }
  };

  useEffect(() => {
    if (selectedImage) {
      setSelectedImageUrl(URL.createObjectURL(selectedImage));
    }
    // eslint-disable-next-line
  }, [selectedImage]);

  useEffect(() => {
    switch (val) {
      case 0:
        setLanguage(xml);
        break;
      case 1:
        setLanguage(javascript);
        break;
      case 2:
        setLanguage(python);
        break;
      case 3:
        setLanguage(php);
        break;
      default:
        break;
    }
  }, [val, setLanguage]);

  function handleEditorChange(value, event) {
    setCode(value);
  }
  const handleChoose = (event, newValue) => {
    setVal(newValue);
  };

  return (
    <>
      <Grid
        item
        xs={12}
        className={
          isTabletOrMobile ? `center-and-align` : `center-and-align p-1`
        }
      >
        <TextField
          label="Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          inputProps={{ maxLength: 50 }}
          required
          variant="outlined"
          fullWidth
        />
      </Grid>
      <Grid item xs={12} className="center-and-align p-1">
        <Time />
      </Grid>
      <Grid
        item
        xs={12}
        className={
          isTabletOrMobile ? `center-and-align` : `center-and-align p-1`
        }
      >
        <ReactQuill
          value={instruction}
          modules={modules}
          formats={formats}
          onChange={setInstruction}
          className="quill-class"
          placeholder={"Write here your instructions for the step"}
        />
      </Grid>
      <Grid item xs={12} className="center-and-start p-1">
        <div>
          <p style={{ margin: selectedImageUrl ? "0" : "0 0 1rem 0" }}>
            {selectedImageUrl ? "Uploaded Image" : "Upload an image"}
          </p>
          <input
            type="file"
            multiple={false}
            accept="image/png, image/jpeg"
            onChange={(e) => handleImageFile(e)}
            id="image-upload"
            style={{ display: selectedImageUrl ? "none" : "block" }}
          />
          <br></br>
          {selectedImageUrl && (
            <div className="center-and-start">
              <div style={{ position: "relative" }}>
                <div className="center border-1-solid-black mt-1">
                  <img
                    alt="Unavailable"
                    src={selectedImageUrl}
                    style={{ maxWidth: "350px", aspectRatio: "16/9" }}
                    onMouseEnter={() => setHoverImage(true)}
                    onMouseLeave={() => setHoverImage(false)}
                  />
                </div>
                <div
                  style={{
                    display: hoverImage ? "block" : "none",
                  }}
                  className="update-image-container"
                  onClick={() => {
                    setSelectedImageUrl("");
                    document.getElementById("image-upload").value = "";
                  }}
                >
                  <CloseIcon />
                </div>
              </div>
            </div>
          )}
        </div>
      </Grid>
      <Grid item xs={12} className="center-and-align p-1">
        <div className="w-100">
          <div className="center-and-end">
            <span className="color-grey">Math &nbsp;</span>
            {visibilityMath ? (
              <VisibilityOff
                onClick={() => setVisibilityMath(false)}
                style={{ cursor: "pointer" }}
              />
            ) : (
              <Visibility
                onClick={() => setVisibilityMath(true)}
                style={{ cursor: "pointer" }}
              />
            )}
          </div>
          {visibilityMath && (
            <TextField
              label="Formula"
              value={formula}
              onChange={(e) => setFormula(e.target.value)}
              placeholder="sum_(i=1)^n i^3=((n(n+1))/2)^2"
              variant="outlined"
              fullWidth
            />
          )}
        </div>
      </Grid>
      <Grid
        item
        xs={12}
        className="center-and-start p-1"
        style={{ display: formula && visibilityMath ? "block" : "none" }}
      >
        <TeX>{formula}</TeX>
      </Grid>
      <Grid item xs={12} className="center-and-align p-1">
        <div className="w-100">
          <div className="center-and-end ">
            <span className="color-grey">Audio &nbsp;</span>
            {visibilityAudio ? (
              <VisibilityOff
                onClick={() => setVisibilityAudio(false)}
                style={{ cursor: "pointer" }}
              />
            ) : (
              <Visibility
                onClick={() => setVisibilityAudio(true)}
                style={{ cursor: "pointer" }}
              />
            )}
          </div>
          {visibilityAudio && (
            <Grid container>
              <Grid
                item
                xs={6}
                md={5}
                className={isTabletOrMobile ? `p-1` : ``}
              >
                <TextField
                  label="Audio file name"
                  value={audioName}
                  onChange={(e) => setAudioName(e.target.value)}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        {" "}
                        <MusicNoteSharpIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>
              <Grid
                item
                xs={6}
                md={7}
                className={
                  isTabletOrMobile ? `center-and-align p-1` : `center-and-align`
                }
              >
                <div>
                  <Input
                    onChange={audioToBase64}
                    inputProps={{ accept: "audio/*" }}
                    type="file"
                    name="file"
                    id="fileAudio"
                    label="Audio"
                    key={inputKey || ""}
                    className="plr-1"
                  />
                  <div className="center-and-end">
                    <span style={{color: "grey"}}>
                      <small >Max 15MB is allowed</small>
                    </span>
                  </div>
                </div>
              </Grid>
            </Grid>
          )}
        </div>
      </Grid>
      <Grid item xs={12} className="center-and-align p-1">
        <div className="w-100">
          <div className="center-and-end">
            <span className="color-grey">Code &nbsp;</span>
            {visibilityCode ? (
              <VisibilityOff
                onClick={() => setVisibilityCode(false)}
                style={{ cursor: "pointer" }}
              />
            ) : (
              <Visibility
                onClick={() => setVisibilityCode(true)}
                style={{ cursor: "pointer" }}
              />
            )}
          </div>
          {visibilityCode && (
            <Grid container>
              <Grid item xs={12}>
                <Paper
                  square
                  style={{ backgroundColor: "white" }}
                  elevation={3}
                >
                  <Tabs
                    value={val}
                    onChange={handleChoose}
                    centered
                    className="color-black"
                  >
                    <Tab label={xml} style={{ minWidth: "25%" }} />
                    <Tab label={javascript} style={{ minWidth: "25%" }} />
                    <Tab label={python} style={{ minWidth: "25%" }} />
                    <Tab label={php} style={{ minWidth: "25%" }} />
                  </Tabs>
                </Paper>
                <Editor
                  height="40vh"
                  defaultLanguage={xml}
                  language={language}
                  defaultValue="<!-- Write something here... -->"
                  theme="vs-dark"
                  value={code}
                  onChange={handleEditorChange}
                />
                {/* <Controlled
                  value={code}
                  onBeforeChange={handleChange}
                  options={{
                    mode: language,
                    theme: "yonce",
                    lineNumbers: true,
                    autoCloseTags: true,
                    autoCloseBrackets: true,
                    matchBrackets: true,
                  }}
                /> */}
              </Grid>
            </Grid>
          )}
        </div>
      </Grid>
      <Grid item xs={12} className="center-and-align p-1">
        <Links
          handleRemoveLink={handleRemoveLink}
          handleAddLink={handleAddLink}
        />
      </Grid>
    </>
  );
}
