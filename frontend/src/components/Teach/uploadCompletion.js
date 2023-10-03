import { Button, Typography, LinearProgress } from "@mui/material";
import { Link } from "@mui/material";
import React, { useContext, useEffect, useState } from "react";
import { GlobalContext } from "../../context/globalContext";

const CLIENT_URL = process.env.REACT_APP_CLIENT_URL;

export default function CompleteUpload({ handleSubmit, uploadProgress, allowUploadProps }) {
  const context = useContext(GlobalContext);
  const { video } = context;
  const { file, title, thumbnailServer } = allowUploadProps;
  const [allowUpload, setAllowUpload] = useState(false); 


  useEffect(() => {
    if(file && title !== "" && thumbnailServer !== "") {
      setAllowUpload(true);
    } else {
      setAllowUpload(false);
    }
  }, [file, title, thumbnailServer])

  
  
  return (
    <div style={{ padding: "1rem" }}>
      <div>
        <h2>Review</h2>
        <p>
          If you are happy with the final result you can then proceed an upload
          your video. <br></br>
          But be careful, you can't edit your video after uploading it, you can
          only change secondary informations like title and description, but not
          the video itself or the thumbnail..
        </p>
      </div>
      <div className="upload-btn-container center-and-align">
      <Button
        variant="contained"
        onClick={handleSubmit}
        className="upload-button"
        style={{
          color: allowUpload ? "white" : "grey",
          backgroundColor: allowUpload ? "black" : "darkgrey",
          borderRadius: "0",
        }}
        disabled={!allowUpload}
        >
        Upload video
      </Button>
        </div>
      <div style={{ marginTop: "1rem" }}>
        {uploadProgress !== 0 && (
          <>
            <div style={{ margin: "0 0 1rem 0" }}>
              {uploadProgress < 100 ? (
                <span>Uploading....{uploadProgress}%</span>
              ) : (
                <>
                  <Typography>
                    Congratulations!<br></br> Your video has been uploaded!
                    <br></br>
                    {video && (
                      <span>
                        Visit it at{" "}
                        <Link href={`${CLIENT_URL}/learn/${video?.id}`}>
                          learn/{video?.id}
                        </Link>
                      </span>
                    )}
                  </Typography>
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
      </div>
    </div>
  );
}
