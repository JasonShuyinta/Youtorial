import React, { useEffect, useState } from "react";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";
import { useMediaQuery } from "react-responsive";
import {
  Container,
  TextField,
  InputAdornment,
  Grid,
  Chip,
  Typography,
} from "@mui/material";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import { uploadFormats,  category, uploadModules } from "../utils/constants";
import ImageIcon from "@mui/icons-material/Image";

function StepOne({ setDuration, handleFile, file }) {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });

  useEffect(() => {
    if (file) {
      document.getElementsByTagName("video")[0].src = file;
      document.getElementsByTagName("video")[0].onloadedmetadata = function () {
        setDuration(document.getElementsByTagName("video")[0].duration * 1000);
      };
    }
    // eslint-disable-next-line
  }, [file]);
  return (
    <>
      {isTabletOrMobile ? (
        <>
          <div style={{ height: "56.25vw" }}>
            {!file ? (
              <div className="teach-container-empty full-width-height">
                <label>
                  <CloudUploadIcon className="upload-icon" />
                  <input
                    type="file"
                    name="file"
                    accept="video/*"
                    style={{ display: "none" }}
                    onChange={handleFile}
                  />
                </label>
              </div>
            ) : (
              <video
                width="100%"
                height="100%"
                controls
                id="uploadedVideo"
              ></video>
            )}
          </div>
        </>
      ) : (
        <>
          {!file ? (
            <div className="teach-container-empty full-width-height">
              <label>
                <CloudUploadIcon className="upload-icon" />
                <input
                  type="file"
                  name="file"
                  accept="video/*"
                  style={{ display: "none" }}
                  onChange={handleFile}
                />
              </label>
            </div>
          ) : (
            <video
              width="100%"
              height="100%"
              controls
              id="uploadedVideo"
            ></video>
          )}
        </>
      )}
    </>
  );
}

function StepTwo({
  title,
  setTitle,
  description,
  setDescription,
  chosenCategories,
  setChosenCategories,
}) {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });

  const [charCounter, setCharCounter] = useState(0);

  const handleCategory = (category) => {
    if (chosenCategories.length < 3) {
      setChosenCategories([...chosenCategories, category]);
      document.getElementById(category).style.display = "none";
    }
  };

  const handleDelete = (categoryChosen) => {
    var tmpArray = [...chosenCategories];
    const index = tmpArray.findIndex((cat) => cat === categoryChosen);
    if (index !== -1) tmpArray.splice(index, 1);
    setChosenCategories(tmpArray);
    document.getElementById(categoryChosen).style.display = "inline-flex";
  };

  return (
    <Container className="teach-step-two">
      <TextField
        value={title}
        onChange={(e) => {
          setTitle(e.target.value);
          setCharCounter(e.target.value.length);
        }}
        variant="outlined"
        type="text"
        label="Title"
        required
        fullWidth
        inputProps={{ maxLength: "100" }}
        InputProps={{
          endAdornment: (
            <InputAdornment position="end" style={{ color: "gray" }}>
              {charCounter}/100
            </InputAdornment>
          ),
        }}
      />
      <ReactQuill
        modules={uploadModules}
        placeholder={"Insert video description here"}
        value={description}
        onChange={setDescription}
        formats={uploadFormats}
        style={{ width: "100%", height: "140px", marginBottom: "4rem", marginTop: "4rem" }}
      />
      <Grid container>
        <Grid item xs={12}>
          <div style={{ margin: "1rem 0" }}>
            <Typography variant="subtitle1">
              You can choose up to 3 categories.
            </Typography>
          </div>
          <div className="uploadChips">
            {category.map((cat) => {
              return (
                <Chip
                  variant="outlined"
                  label={cat}
                  onClick={() => handleCategory(cat)}
                  id={cat}
                  key={cat}
                  className="teach-step-two-chip"
                />
              );
            })}
          </div>
        </Grid>
        <Grid item xs={12}>
          <div
            style={{ margin: isTabletOrMobile ? "0 0 1rem 0" : "1rem 0 0 0" }}
          >
            {chosenCategories.length !== 0 &&
              chosenCategories.map((cat) => {
                return (
                  <Chip
                    variant="outlined"
                    label={cat}
                    key={cat}
                    onDelete={() => handleDelete(cat)}
                    className="teach-step-two-categories"
                  />
                );
              })}
          </div>
        </Grid>
      </Grid>
    </Container>
  );
}

function StepThree({ thumbnail, handleThumbnail }) {
  return (
    <div style={{ width: "100%", height: "100%" }}>
      {!thumbnail ? (
        <div className="teach-container-empty full-width-height">
          <label>
            <ImageIcon className="upload-icon" />
            <input
              type="file"
              name="file"
              accept="image/*"
              style={{ display: "none" }}
              onChange={handleThumbnail}
            />
          </label>
        </div>
      ) : (
        <div style={{ width: "100%", height: "100%" }}>
          <img src={thumbnail} alt="thumbnail" width="100%" height="100%" />
        </div>
      )}
    </div>
  );
}

function StepFour({ video }) {
  return (
    <>
      <div className="full-width-height">
        <video
          controls
          src={video}
          width="100%"
          height="100%"
          id="video"
          className="video-preview-teach"
        />
      </div>
    </>
  );
}

export { StepOne, StepTwo, StepThree, StepFour };
