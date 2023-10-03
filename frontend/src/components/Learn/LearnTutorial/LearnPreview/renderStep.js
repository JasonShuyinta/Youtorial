import React, { useState } from "react";
import {
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Grid,
  Tooltip,
  IconButton,
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import parse from "html-react-parser";
import Notes from "../Utils/notes";
import Links from "../Utils/links";
import Time from "../Utils/time";
import Editor from "@monaco-editor/react";
import FileCopyIcon from "@mui/icons-material/FileCopy";
import MusicNoteSharpIcon from "@mui/icons-material/MusicNoteSharp";
import ReactAudioPlayer from "react-audio-player";
import 'katex/dist/katex.min.css';
import TeX from '@matejmazur/react-katex';
import CheckStep from "../Utils/checkStep";
import Modify from "../Utils/modify";

export default function RenderStep({ step, preview, handleDeleteStep }) {
  var readingStep = step;

  const [copied, setCopied] = useState(false);

  const handleCopyToClip = () => {
    navigator.clipboard.writeText(readingStep.code);
    setCopied(true);
  };

  return (
    <Accordion
      key={readingStep.id}
      id={readingStep.id}
      style={{ border: "1px solid #C8C8C8" }}
    >
      <AccordionSummary expandIcon={<ExpandMoreIcon />}>
        <CheckStep
          title={readingStep.title}
          stepId={readingStep.id}
          startTime={readingStep.startTime}
          preview={preview}
        />
      </AccordionSummary>
      <AccordionDetails>
        <Grid container>
          <Time
            startTime={readingStep.startTime}
            endTime={readingStep.endTime}
          />
          <Grid item xs={12}>
            <span className="instruction">
              {" "}
              {/* {ReactHtmlParser(readingStep.instruction)}{" "} */}
              {parse(readingStep.instruction)}
            </span>
          </Grid>
          {readingStep.code && (
            <Grid item xs={12}>
              <div className="w-100 text-right">
                <Tooltip
                  PopperProps={{ disablePortal: true }}
                  id="tooltip"
                  placement="left"
                  arrow
                  onClose={() => setCopied(false)}
                  open={copied}
                  disableFocusListener
                  disableTouchListener
                  title="Copied to clipboard!"
                >
                  <IconButton
                    onClick={handleCopyToClip}
                    style={{ margin: "-1rem -0.5rem 0 -0.5rem" }}
                  >
                    <FileCopyIcon />
                  </IconButton>
                </Tooltip>
              </div>
              <Editor
                height="40vh"
                language={step.language}
                value={readingStep.code}
                theme="vs-dark"
              />
            </Grid>
          )}
          {readingStep.image && (
            <Grid item xs={12} className="center mtb-1">
              <img
                src={readingStep?.image}
                alt="Not found"
                style={{ maxWidth: "350px", aspectRatio: "16/9" }}
              />
            </Grid>
          )}
          {readingStep.formula && (
            <Grid item xs={12} className="pt-1">
              <TeX>{readingStep.formula}</TeX>
            </Grid>
          )}
          {readingStep.audioFile && (
            <Grid item xs={12} className="mt-1">
              <ReactAudioPlayer
                src={readingStep.audioFile}
                id="audioPlayer"
                controls
                className="w-100"
              />
              <div className="disp-flex mt-1">
                <MusicNoteSharpIcon />
                <p className="m-0">{readingStep?.audioName}</p>
              </div>
            </Grid>
          )}
          <Links links={readingStep?.links} />
          <Notes preview={preview} stepId={readingStep.id} />
          <Modify
            preview={preview}
            handleDeleteStep={handleDeleteStep}
            stepId={readingStep.id}
          />
        </Grid>
      </AccordionDetails>
    </Accordion>
  );
}
