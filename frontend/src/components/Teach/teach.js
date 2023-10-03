import { Grid } from "@mui/material";
import aws from "aws-sdk";
import { v4 as uuidv4 } from "uuid";
import axios from "axios";
import React, { useContext, useState } from "react";
import { GlobalContext } from "../../context/globalContext";
import { InsertInfo, InsertThumbnail, UploadVideo } from "./infoInsertion";
import CompleteUpload from "./uploadCompletion";
import { StepOne, StepTwo, StepThree, StepFour } from "./teachingSteps";
import UploadVideoInfo from "./uploadVideoInfo";
import LearnCreate from "../Learn/LearnTutorial/LearnCreate/learnCreate";
import LearnPreview from "../Learn/LearnTutorial/LearnPreview/learnPreview";
import Unlogged from "../Account/unlogged";
import PartialStepper from "../utils/partialStepper";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;
const ACCESSKEYID = process.env.REACT_APP_ACCESSKEYID;
const SECRETKEY = process.env.REACT_APP_SECRETKEY;
const BUCKETNAME = process.env.REACT_APP_BUCKETNAME;

const s3 = new aws.S3({ accessKeyId: ACCESSKEYID, secretAccessKey: SECRETKEY });

export default function Teach() {
  const context = useContext(GlobalContext);
  const { loggedUser, config, setVideo, video } = context;

  const [currentStep, setCurrentStep] = useState(0);
  const [duration, setDuration] = useState("");
  const [file, setFile] = useState(null);
  const [previewUrl, setPreviewUrl] = useState("");
  const [arrayBuffer, setArrayBuffer] = useState([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [chosenCategories, setChosenCategories] = useState([]);
  const [thumbnail, setThumbnail] = useState("");
  const [thumbnailServer, setThumbnailServer] = useState("");
  const [uploadProgress, setUploadProgress] = useState(0);
  const [videoCorrectlyUploaded, setVideoCorrectlyUploaded] = useState(false);

  const handleFile = (e) => {
    setFile(e.target.files[0]);
    setPreviewUrl(URL.createObjectURL(e.target.files[0]));
    var reader = new FileReader();
    reader.readAsArrayBuffer(e.target.files[0]);
    reader.onload = function (e) {
      setArrayBuffer(reader.result);
    };
  };

  const handleThumbnail = (e) => {
    setThumbnail(URL.createObjectURL(e.target.files[0]));
    var reader = new FileReader();
    reader.readAsDataURL(e.target.files[0]);
    reader.onload = function (e) {
      setThumbnailServer(reader.result);
    };
  };

  const handleSubmit = () => {
    let myFile = file.name.split(".");
    const fileType = myFile[myFile.length - 1];
    const fileName = `${uuidv4()}.${fileType}`;
    const params = {
      Bucket: BUCKETNAME,
      Key: fileName,
      Body: arrayBuffer,
    };
    s3.upload(params, (error, data) => {
      if (error) console.log(error);

      axios
        .post(
          `${BASE_URL}/video`,
          {
            title,
            description,
            location: data.Location,
            thumbnail: thumbnailServer,
            duration: duration,
            category: chosenCategories,
          },
          config
        )
        .then((res) => {
          if (res.status === 200) {
            setVideoCorrectlyUploaded(true);
            setVideo(res.data);
          } else {
            alert("Something went wrong, please try again later");
          }
        })
        .catch((err) => console.log(err));
    }).on("httpUploadProgress", function (evt) {
      setUploadProgress(parseInt((evt.loaded * 100) / evt.total));
    });
  };

  const handleNextStep = () => {
    setCurrentStep((prevActiveStep) => prevActiveStep + 1);
  };

  const handleBackStep = () => {
    setCurrentStep((prevActiveStep) => prevActiveStep - 1);
  };

  function showStep() {
    switch (currentStep) {
      case 0:
        return (
          <StepOne
            setDuration={setDuration}
            handleFile={handleFile}
            file={previewUrl}
          />
        );
      case 1:
        return (
          <StepTwo
            title={title}
            setTitle={setTitle}
            description={description}
            setDescription={setDescription}
            chosenCategories={chosenCategories}
            setChosenCategories={setChosenCategories}
          />
        );
      case 2:
        return (
          <StepThree thumbnail={thumbnail} handleThumbnail={handleThumbnail} />
        );
      case 3:
        return <StepFour video={previewUrl} />;
      case 4:
        return <StepFour video={previewUrl} />;
      case 5:
        return <StepFour video={previewUrl} />;
      default:
        break;
    }
  }

  function slideInComponent() {
    switch (currentStep) {
      case 0:
        return <UploadVideo />;
      case 1:
        return <InsertInfo />;
      case 2:
        return <InsertThumbnail />;
      case 3:
        return (
          <CompleteUpload
            allowUploadProps={allowUploadProps}
            handleSubmit={handleSubmit}
            uploadProgress={uploadProgress}
          />
        );
      case 4:
        return <LearnCreate video={video} />;
      case 5:
        return <LearnPreview video={video} currentStep={currentStep} />;
      default:
        break;
    }
  }

  const recurrentProps = { title, description, chosenCategories };
  const allowUploadProps = { file, title, thumbnailServer };

  return (
    <div className="min-height-container">
      {loggedUser ? (
        <Grid container className="teach-step-container">
          <Grid item xs={12} sm={7} className="teach-step-subcontainer">
            <div className="video-ratio">{showStep()}</div>
            <div
              style={{
                display:
                  currentStep === 3 || currentStep === 4 || currentStep === 5
                    ? "block"
                    : "none",
              }}
            >
              <UploadVideoInfo recurrentProps={recurrentProps} />
            </div>
          </Grid>
          <Grid item xs={12} sm={5} className="teach-step-supcontainer">
            <div className="teach-info-container">
              <div className="teach-info-subcontainer">
                {slideInComponent()}
              </div>
              <PartialStepper
                videoCorrectlyUploaded={videoCorrectlyUploaded}
                currentStep={currentStep}
                handleBackParticularStep={handleBackStep}
                handleNextParticularStep={handleNextStep}
              />
            </div>
          </Grid>
        </Grid>
      ) : (
        <Unlogged />
      )}
    </div>
  );
}
