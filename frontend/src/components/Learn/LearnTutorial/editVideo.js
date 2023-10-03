import React, { useState, useEffect, useContext } from "react";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import {
  Typography,
  Box,
  Grid,
  TextField,
  Chip,
  InputAdornment,
  Button,
} from "@mui/material";
import { useParams } from "react-router-dom";
import axios from "axios";
import { GlobalContext } from "../../../context/globalContext";
import { useMediaQuery } from "react-responsive";
import { formats, modules, category } from "../../utils/constants";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function EditVideo() {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });
  const params = useParams();
  const [videoTitle, setVideoTitle] = useState("");
  const [videoDescription, setVideoDescription] = useState("");
  const [videoCategories, setVideoCategories] = useState([]);
  const [altCategories, setAltCategories] = useState([]);
  const [charCounter, setCharCounter] = useState(0);
  const [videoInfo, setVideoInfo] = useState({});

  const context = useContext(GlobalContext);
  const { successfulUpdate, setSuccessfulUpdate } = context;

  useEffect(() => {
    axios
      .get(
        `${BASE_URL}/video/videoInfo/${params.videoId}`
      )
      .then((res) => {
        setVideoInfo(res.data);
        setVideoTitle(res.data.title);
        setVideoDescription(res.data.description);
        setVideoCategories(res.data.category);
        res.data.category &&
          res.data.category.length > 0 &&
          setAltCategories(
            category.filter((cat) => !res.data.category.includes(cat))
          );
      })
      .catch((err) => console.log(err));
  }, [params.videoId]);

  const handleDeleteCategory = (cat) => {
    setVideoCategories(videoCategories.filter((c) => c !== cat));
    setAltCategories([...altCategories, cat]);
  };

  const handleAddCategory = (cat) => {
    if (videoCategories.length < 3) {
      setVideoCategories([...videoCategories, cat]);
      setAltCategories(altCategories.filter((c) => c !== cat));
    } else {
      alert("You can only select 3 categories");
    }
  };

  const handleSaveUpdatedInfo = () => {
    axios
      .put(`${BASE_URL}/video`, {
        id: videoInfo.id,
        title: videoTitle,
        description: videoDescription,
        url: videoInfo.url,
        thumbnail: videoInfo.thumbnail,
        duration: videoInfo.duration,
        category: videoCategories,
        location: videoInfo.location,
      })
      .then((res) => {
        if(res.status === 200) {
            setSuccessfulUpdate(!successfulUpdate);
        }
      }).catch((err) => console.log(err));
  }

  return (
    <div style={{ minHeight: isTabletOrMobile ? "160vh" : "120vh" }}>
      <Box className="full-width-height">
        <Grid container style={{ padding: "1rem" }}>
          <Grid item xs={12} md={7}>
            <div className="dialog-video">
              <video
                src={videoInfo?.location}
                alt="video"
                controls
                poster={videoInfo?.thumbnail}
                style={{ width: "100%", height: "auto" }}
              />
            </div>
          </Grid>
          <Grid item xs={12} md={5}>
            <div className="ptb-2 plr-1">
              <TextField
                label="Title"
                variant="outlined"
                value={videoTitle}
                fullWidth
                onChange={(e) => {
                  setCharCounter(e.target.value.length);
                  setVideoTitle(e.target.value);
                }}
                inputProps={{ maxLength: "100" }}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end" style={{ color: "gray" }}>
                      {charCounter}/100
                    </InputAdornment>
                  ),
                }}
              />
              <div className="mt-2" style={{ height: "25vh" }}>
                <ReactQuill
                  value={videoDescription}
                  modules={modules}
                  formats={formats}
                  onChange={setVideoDescription}
                  className="quill-class"
                />
              </div>
              <div className="mt-2">
                <Typography>Edit your categories</Typography>
                {videoCategories && (
                  <div className="dialog-video-chip-container">
                    {videoCategories.length > 0 &&
                      videoCategories.map((cat, index) => {
                        return (
                          <Chip
                            key={index}
                            label={cat}
                            variant="outlined"
                            onDelete={() => handleDeleteCategory(cat)}
                            style={{
                              marginRight: "1rem",
                            }}
                          />
                        );
                      })}
                  </div>
                )}
              </div>
              <div className="dialog-video-chip-container">
                {altCategories.map((altCat, index) => {
                  return (
                    <Chip
                      label={altCat}
                      key={index}
                      onClick={() => handleAddCategory(altCat)}
                      variant="outlined"
                      style={{ marginRight: "1rem" }}
                    />
                  );
                })}
              </div>
              <div className="btn-save-container">
                <Button
                  onClick={handleSaveUpdatedInfo}
                  variant="contained"
                  className="btn-save"
                  fullWidth
                >
                  {" "}
                  Save{" "}
                </Button>
              </div>
            </div>
          </Grid>
        </Grid>
      </Box>
    </div>
  );
}
