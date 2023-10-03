import React from "react";
import { MobileStepper, Button } from "@mui/material";
import KeyboardArrowLeft from "@mui/icons-material/KeyboardArrowLeft";
import KeyboardArrowRight from "@mui/icons-material/KeyboardArrowRight";
import { useTheme } from "@mui/material/styles";

export default function PartialStepper({
  currentStep,
  handleNextParticularStep,
  handleBackParticularStep,
  edit,
  videoCorrectlyUploaded
}) {
  const theme = useTheme();

  return (
    <MobileStepper
      steps={edit ? 2 : 6}
      position="static"
      variant="text"
      activeStep={currentStep}
      className="partial-stepper"
      nextButton={
        <Button
          size="small"
          onClick={() => handleNextParticularStep()}
          disabled={currentStep === 5 || (currentStep === 3 && !videoCorrectlyUploaded) }
          className={ currentStep === 5 || (currentStep === 3 && !videoCorrectlyUploaded) ? "colorGrey" : "colorBtnBlack"}
        >
          Next
          {theme.direction === "rtl" ? (
            <KeyboardArrowLeft className= "colorBtnBlack"  />
          ) : (
            <KeyboardArrowRight className={currentStep === 5 || (currentStep === 3 && !videoCorrectlyUploaded) ? "colorGrey" : "colorBtnBlack"} />
          )}
        </Button>
      }
      backButton={
        <Button
          size="small"
          onClick={() => handleBackParticularStep()}
          disabled={currentStep === 0 || (currentStep === 4 && videoCorrectlyUploaded)}
          className={currentStep === 0 ? "colorGrey" : "colorBtnBlack"}
        >
          {theme.direction === "rtl" ? (
            <KeyboardArrowRight className="colorBtnBlack" />
          ) : (
            <KeyboardArrowLeft className={ currentStep === 0 ? "colorGrey" : "colorBtnBlack"} />
          )}
          Back
        </Button>
      }
    />
  );
}
