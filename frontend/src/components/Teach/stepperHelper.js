import React from "react";
import {
  Button,
  LinearProgress,
  Link,
  Step,
  StepContent,
  StepLabel,
  Stepper,
  Typography,
} from "@mui/material";
import { stepContent, steps } from "../constants";

const CLIENT_URL = process.env.REACT_APP_CLIENT_URL;

export default function StepperHelper({
  currentStep,
  file,
  uploadProgress,
  videoId,
  handleFile,
  thumbnailServer,
  handleThumbnail,
  setCurrentStep,
  handleNext,
  previewUrl,
  savedInfo,
  handleCreateTutorial,
}) {
  return (
    <Stepper activeStep={currentStep} orientation="vertical">
      {steps.map((label) => (
        <Step key={label}>
          <StepLabel>{label}</StepLabel>
          <StepContent>
            <Typography>{stepContent[currentStep]}</Typography>
            <div>
              <Button
                style={{
                  backgroundColor: "#7d88ea",
                  display: file && currentStep === 0 ? "flex" : "none",
                }}
              >
                <label style={{ color: "white" }}>
                  Edit
                  <input
                    type="file"
                    name="file"
                    accept="video/*"
                    style={{ display: "none" }}
                    onChange={handleFile}
                  />
                </label>
              </Button>
              <Button
                style={{
                  backgroundColor: "#7d88ea",
                  display:
                    thumbnailServer && currentStep === 2 ? "flex" : "none",
                }}
              >
                <label style={{ color: "white" }}>
                  Edit
                  <input
                    type="file"
                    name="file"
                    accept="image/*"
                    style={{ display: "none" }}
                    onChange={handleThumbnail}
                  />
                </label>
              </Button>
            </div>
            <div className="upload-buttons">
              <Button
                style={{
                  backgroundColor: currentStep === 0 && "#e0e0e0",
                  color: "white",
                }}
                disabled={currentStep === 0}
                onClick={() =>
                  setCurrentStep((prevActiveStep) => prevActiveStep - 1)
                }
              >
                Back
              </Button>
              <Button
                variant="contained"
                onClick={handleNext}
                disabled={
                  (currentStep === 0 && previewUrl === "") ||
                  (currentStep === 1 && !savedInfo)
                }
                style={{ marginRight: "2rem" }}
              >
                {currentStep === steps.length - 1 ? "Upload" : "Next"}
              </Button>
            </div>
            <div style={{ marginTop: "1rem" }}>
              {currentStep === steps.length - 1 && (
                <>
                  {uploadProgress !== 0 && (
                    <>
                      <div style={{ margin: "0 0 1rem 0" }}>
                        {uploadProgress < 100 ? (
                          <span>Uploading....{uploadProgress}%</span>
                        ) : (
                          <>
                            <Typography>
                              Congratulations!<br></br> Your video has been
                              uploaded!
                              <br></br>
                              {videoId && (
                                <span>
                                  Visit it at{" "}
                                  <Link href={`${CLIENT_URL}/learn/${videoId}`}>
                                    learn/{videoId}
                                  </Link>
                                </span>
                              )}
                            </Typography>
                            <Button
                              variant="contained"
                              onClick={handleCreateTutorial}
                            >
                              Create tutorial
                            </Button>
                          </>
                        )}
                      </div>
                      <LinearProgress
                        variant="determinate"
                        value={parseInt(uploadProgress)}
                        style={{
                          width: "100%",
                          margin: "auto",
                          display: uploadProgress !== 100 ? "block" : "none",
                        }}
                      />
                    </>
                  )}
                </>
              )}
            </div>
          </StepContent>
        </Step>
      ))}
    </Stepper>
  );
}
