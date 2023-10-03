import { Card, Grid, TextField, Typography } from "@mui/material";
import React from "react";
import { useMediaQuery } from "react-responsive";

export default function Instructions() {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });

  return (
    <>
      <section className="section-instruction">
        {isTabletOrMobile ? (
          <>
            <div style={{ justifyContent: "center" }}>
              <Typography variant="h4">How easy it really is?</Typography>
            </div>
            <Grid container className="instructions-container">
              <Grid item xs={12} align="center">
                <Typography variant="h5" style={{ marginBottom: "2rem" }}>
                  <b>1.</b> Upload your video and share your knowledge with the
                  world
                </Typography>
                <Card
                  elevation={3}
                  className="center instructions-img-container"
                >
                  <div style={{ borderRadius: "1px solid grey" }}>
                    <img
                      src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/up.png"
                      className="section-instruction-img"
                      alt="upload"
                    />
                  </div>
                </Card>
              </Grid>
              <Grid item xs={12} align="center" style={{ paddingTop: "4rem" }}>
                <Typography variant="h5" style={{ marginBottom: "2rem" }}>
                  <b>2.</b> Create some steps to guide your viewers through the
                  video
                </Typography>
                <Card elevation={3} className="instructions-card">
                  <TextField
                    label="Type"
                    value="Standard"
                    fullWidth
                    disabled
                    className="section-instruction-textfield"
                  />
                  <TextField
                    label="Title"
                    value="Step 1 - Title"
                    fullWidth
                    disabled
                    className="section-instruction-textfield"
                  />
                  <TextField
                    label="Instructions"
                    value="Instructions to better understand the step"
                    multiline
                    fullWidth
                    disabled
                    rows={5}
                    style={{ fontSize: "small", fontStyle: "italic" }}
                  />
                </Card>
              </Grid>
              <Grid item xs={12} align="center">
                <Typography variant="h5" style={{ margin: "3rem 0" }}>
                  <b>3.</b> Learn or teach in a simple and easy way!
                </Typography>
                <Card elevation={3} className="center card-size">
                  <Grid container>
                    <Grid item xs={6} className="center">
                      <div className="center card-img">
                        <img
                          src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/video.png"
                          alt="video"
                        />
                      </div>
                    </Grid>
                    <Grid item xs={6}>
                      <Grid container direction="column" style={{borderLeft: "1px solid black"}}>
                        <div className="section-instruction-step-mobile">
                          <Typography>Step 1</Typography>
                        </div>
                        <div className="section-instruction-step-mobile">
                          <Typography>Step 2</Typography>
                        </div>
                        <div className="section-instruction-step-mobile">
                          <Typography>Step 3</Typography>
                        </div>
                        <div className="section-instruction-step-mobile">
                          <Typography>Step 4</Typography>
                        </div>
                        <div className="section-instruction-step-mobile">
                          <Typography>Step 5</Typography>
                        </div>
                      </Grid>
                    </Grid>
                  </Grid>
                </Card>
              </Grid>
            </Grid>
          </>
        ) : (
          <section style={{ paddingTop: "5rem" }}>
            <div>
              <Typography variant="h3">How easy it really is?</Typography>
            </div>
            <Grid container style={{ margin: "5rem 0" }}>
              <Grid item xs={4} align="center">
                <Card elevation={3} className="center section-instruction-card">
                  <div className="card-instructions">
                    <img
                      src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/up.png"
                      className="section-instruction-img"
                      alt="upload"
                    />
                  </div>
                </Card>
                <Typography variant="h5" style={{ width: "200px" }}>
                  Upload your video and share your knowledge with the world
                </Typography>
              </Grid>
              <Grid item xs={4} align="center" style={{ paddingTop: "4rem" }}>
                <Typography
                  variant="h6"
                  style={{ width: "200px", marginBottom: "2rem" }}
                >
                  Create some steps to guide your viewers through the video
                </Typography>
                <Card
                  elevation={3}
                  className="center section-instruction-card card-instruction-spacing "
                >
                  <div>
                    <TextField
                      label="Type"
                      value="Standard"
                      fullWidth
                      disabled
                      className="section-instruction-textfield"
                    />
                    <TextField
                      label="Title"
                      value="Step 1 - Title"
                      fullWidth
                      disabled
                      className="section-instruction-textfield"
                    />
                    <TextField
                      label="Instructions"
                      value="Some instructions to better understand the step"
                      multiline
                      fullWidth
                      disabled
                      rows={5}
                      style={{ fontSize: "small", fontStyle: "italic" }}
                    />
                  </div>
                </Card>
              </Grid>
              <Grid item xs={4} align="center">
                <Card elevation={3} className="center section-instruction-card">
                  <Grid container>
                    <Grid item xs={6} className="center">
                      <div className="center card-img">
                        <img
                          src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/video.png"
                          alt="video"
                        />
                      </div>
                    </Grid>
                    <Grid item xs={6} style={{ borderLeft: "1px solid black" }}>
                      <Grid container direction="column">
                        <Grid item xs={12}>
                          <div className="section-instruction-step-mobile">
                            <Typography>Step 1</Typography>
                          </div>
                        </Grid>
                        <Grid item xs={12}>
                          <div className="section-instruction-step-mobile">
                            <Typography>Step 2</Typography>
                          </div>
                        </Grid>
                        <Grid item xs={12}>
                          <div className="section-instruction-step-mobile">
                            <Typography>Step 3</Typography>
                          </div>
                        </Grid>
                        <Grid item xs={12}>
                          <div className="section-instruction-step-mobile">
                            <Typography>Step 4</Typography>
                          </div>
                        </Grid>
                        <Grid item xs={12}>
                          <div className="section-instruction-step-mobile">
                            <Typography>Step 5</Typography>
                          </div>
                        </Grid>
                      </Grid>
                    </Grid>
                  </Grid>
                </Card>
                <Typography variant="h5" style={{ width: "200px" }}>
                  Learn or teach in a simple and easy way!
                </Typography>
              </Grid>
            </Grid>
          </section>
        )}
      </section>
    </>
  );
}
