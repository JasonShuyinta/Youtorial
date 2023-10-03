import React, { useState, useContext, useEffect } from "react";
import {
  Dialog,
  DialogContent,
  Button,
  Grow,
  AppBar,
  Toolbar,
  IconButton,
  TextField,
  InputAdornment,
  Grid,
  Paper,
  Tabs,
  Tab,
  Input,
  Typography,
} from "@mui/material";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import { BiExitFullscreen } from "react-icons/bi";
import {
  fullScreenFormats,
  fullScreenModules,
  xml,
  javascript,
  php,
  python,
  go,
  rust,
  sql,
} from "../../../../utils/constants";
import TimeFullView from "./timeFullView";
import LinkFullView from "./linkFullView";
import { CreationContext } from "../../../../../context/creationContext";
import 'katex/dist/katex.min.css';
import TeX from '@matejmazur/react-katex';
import Editor from "@monaco-editor/react";
import MusicNoteSharpIcon from "@mui/icons-material/MusicNoteSharp";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Grow direction="up" ref={ref} {...props} />;
});

export default function FullScreenView({ video }) {
  const create = useContext(CreationContext);
  const {
    title,
    requisites,
    instruction,
    fullScreen,
    setTitle,
    setInstruction,
    code,
    setCode,
    language,
    setLanguage,
    formula,
    setFormula,
    setFullScreen,
    selectedImageUrl,
    setSelectedImageUrl,
    setCookingImage,
    timeError,
    overTimeError,
    handleAddLink,
    handleRemoveLink,
    handleSubmit,
    setAudioFile,
    audioName,
    setAudioName,
    inputKey,
  } = create;

  const [characterCounter, setCharacterCounter] = useState(title.length);
  const [val, setVal] = useState(0);
  const [selectedImage, setSelectedImage] = useState(null);

  function handleEditorChange(value, event) {
    setCode(value);
  }
  const handleChoose = (event, newValue) => {
    setVal(newValue);
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
      case 4:
        setLanguage(go);
        break;
      case 5:
        setLanguage(rust);
        break;
      case 6:
        setLanguage(sql);
        break;
      default:
        break;
    }
    // eslint-disable-next-line
  }, [val]);

  const audioToBase64 = (e) => {
    return new Promise((resolve, reject) => {
      let reader = new FileReader();
      reader.onerror = reject;
      reader.readAsDataURL(e.target.files[0]);
      reader.onload = function (e) {
        setAudioFile(reader.result);
      };
    });
  };

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

  return (
    <Dialog
      fullScreen
      TransitionComponent={Transition}
      open={fullScreen}
      onClose={() => setFullScreen(false)}
      scroll="paper"
    >
      <AppBar className="edit-appbar">
        <Toolbar>
          <div className="w-100 color-white">
            <h3>Create a step for your tutorial</h3>
          </div>
          <IconButton onClick={() => setFullScreen(false)} className="m-0 p-0">
            {" "}
            <BiExitFullscreen style={{ color: "white" }} />{" "}
          </IconButton>
        </Toolbar>
      </AppBar>
      <DialogContent>
        <Grid container spacing={2}>
          <Grid item xs={12} md={7} className="p-1 text-center">
            <TextField
              label="Title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              onKeyUp={(e) => setCharacterCounter(e.target.value.length)}
              inputProps={{ maxLength: 50 }}
              required
              variant="outlined"
              fullWidth
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end" className="color-grey">
                    {characterCounter}/50
                  </InputAdornment>
                ),
              }}
            />
          </Grid>
          <Grid item xs={12} md={5} className="p-1 text-center">
            <TimeFullView video={video} />
          </Grid>

          <Grid
            item
            xs={12}
            md={7}
            className="p-1"
            style={{ marginTop: "-1rem" }}
          >
            <ReactQuill
              value={instruction}
              modules={fullScreenModules}
              formats={fullScreenFormats}
              onChange={(e) => setInstruction(e)}
              placeholder={"Write here your instructions for the step"}
              style={{ height: "220px", margin: "1rem auto 3rem auto" }}
            />
          </Grid>
          <Grid item xs={12} md={5} className="fullScreenLinks">
            <div className="text-left mt-2">
              <h5 style={{ margin: "0" }}>
                You can add up to 5 links for each step
              </h5>
            </div>
            <LinkFullView
              requisites={requisites}
              handleAddLink={handleAddLink}
              handleRemoveLink={handleRemoveLink}
            />
          </Grid>
          <Grid item xs={12} className="center-and-start p-1">
            <div>
              <p style={{ margin: "0 0 1rem 0" }}>Upload an image</p>
              <input
                type="file"
                multiple={false}
                accept="image/png, image/jpeg"
                onChange={(e) => handleImageFile(e)}
                id="image-upload"
              />
              <br></br>
              {selectedImageUrl && (
                <div className="center mt-1 border-1-solid-black">
                  <img
                    alt="not found"
                    src={selectedImageUrl}
                    style={{ maxWidth: "350px", aspectRatio: "16/9" }}
                  />
                </div>
              )}
            </div>
          </Grid>
          <Grid item xs={12} className="coding-grid-full-screen">
            <Typography
              variant="body1"
              className="center-and-start color-grey"
              style={{ marginBottom: "0.5rem" }}
            >
              Code
            </Typography>
            <Paper square elevation={3} style={{ backgroundColor: "white" }}>
              <Tabs
                value={val}
                onChange={handleChoose}
                centered
                style={{ color: "black" }}
              >
                <Tab label={xml} />
                <Tab label={javascript} />
                <Tab label={python} />
                <Tab label={php} />
                <Tab label={go} />
                <Tab label={rust} />
                <Tab label={sql} />
              </Tabs>
            </Paper>
          </Grid>
          <Grid item xs={12} style={{ marginBottom: "-2rem" }}>
            <Editor
              height="40vh"
              defaultLanguage={xml}
              language={language}
              value={code}
              onChange={handleEditorChange}
              defaultValue="<!-- Write something here... -->"
              theme="vs-dark"
            />
          </Grid>
          <Grid item xs={12} className="center-and-align p-1 mt-2">
            <div className="w-100">
              <Typography
                variant="body1"
                className="center-and-start color-grey"
                style={{ marginBottom: "0.5rem" }}
              >
                Formula
              </Typography>
              <TextField
                label="Formula"
                value={formula}
                onChange={(e) => setFormula(e.target.value)}
                placeholder="sum_(i=1)^n i^3=((n(n+1))/2)^2"
                variant="outlined"
                fullWidth
              />
            </div>
          </Grid>
          <Grid item xs={12} style={{ display: formula ? "block" : "none" }}>
            <TeX>{formula}</TeX>
          </Grid>
          <Grid item xs={12} className="center-and-align">
            <div className="w-100">
              <Typography
                variant="body1"
                className="center-and-start color-grey"
                style={{ marginBottom: "0.5rem" }}
              >
                Audio
              </Typography>
              <div className="center-and-align">
                <div className="center-and-start w-50">
                  <TextField
                    label="Name"
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
                    fullWidth
                  />
                </div>
                <div className="center-and-align w-50">
                  <Input
                    onChange={audioToBase64}
                    key={inputKey || ""}
                    inputProps={{ accept: "audio/*" }}
                    type="file"
                  />
                </div>
              </div>
              {/*<p style={{fontSize: "13px"}}><small>*For security reasons you need to re-upload your audio when modifying a step</small></p>*/}
            </div>
          </Grid>
          <Grid item xs={12} className="p-1 text-center">
            <Button
              onClick={handleSubmit}
              disabled={timeError || overTimeError}
              variant="outlined"
              className="btn-save"
              fullWidth
            >
              Save
            </Button>
          </Grid>
        </Grid>
      </DialogContent>
    </Dialog>
  );
}
