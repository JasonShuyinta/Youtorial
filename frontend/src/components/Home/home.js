import { Avatar, Button, Grid, Typography } from "@mui/material";
import React from "react";
import { useHistory } from "react-router-dom";
import Instructions from "./instructions";
import { useMediaQuery } from "react-responsive";

export default function Home() {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 500px)" });

  const history = useHistory();

  

  return (
    <>
      {isTabletOrMobile ? (
        <div className="home-container">
          <div className="mobile-section-container">
            <section className="mobile-section-one">
              <div className="title-container">
                <Typography variant="h2">YouTorial</Typography>
                <Typography variant="h3">The future is yours!</Typography>
                <div>
                  <Button
                    variant="contained"
                    onClick={() => history.push("/feed")}
                    className="submit-btn"
                  >
                    Start
                  </Button>
                </div>
              </div>
            </section>
            <Instructions />

            <section className="mobile-section-two">
              <Grid container>
                <Grid item xs={6} style={{ height: "50%" }}>
                  <Typography variant="h5">Sky's the limit!</Typography>
                  <Typography variant="subtitle1">
                    Learn from tutors and teachers from all around the world
                    about the most various subjects, from coding to cooking up
                    to music production.
                    <br></br> Be a student or be a teacher. The choice is yours!
                  </Typography>
                </Grid>
                <Grid item xs={6} style={{ height: "50%" }}>
                  <img
                    src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/webp/online-class.webp"
                    alt="onlineclass"
                    loading="lazy"
                  />
                </Grid>
                <Grid item xs={6}>
                  <img
                    src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/webp/learning.webp"
                    alt="5"
                    loading="lazy"
                  />
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="subtitle1">
                    Build useful skills for school, work, university or just for
                    the sake of learning something new. Easy-to-use interface
                    and limitless functionalities...
                  </Typography>
                </Grid>
              </Grid>
            </section>
            <section className="mobile-section-three">
              <Grid container>
                <Grid item xs={12}>
                  <Typography variant="h3">Get Ready for Take‑Off</Typography>
                  <Typography variant="h6">
                    Give yourself an upgrade and join our growing community of
                    learners and teachers
                  </Typography>
                  <img
                    src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/webp/clipart-rocket-9.webp"
                    alt="rocket"
                    loading="lazy"
                  />
                </Grid>
                <Grid item xs={12} className="mobile-section-three-opinions">
                  <div>
                    <div
                      className="center"
                      style={{
                        width: "100%",
                      }}
                    >
                      <Avatar
                        src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/webp/12.webp"
                        className="mobile-section-three-avatar"
                        alt="12.jpg"
                        loading="lazy"
                      />
                    </div>
                    <Typography variant="h5">Mike</Typography>
                    <Typography variant="subtitle1">
                      University of Bologna
                    </Typography>
                    <div className="separator-mobile"></div>
                    <Typography variant="subtitle2">
                      "It’s a really big deal to offer an online MBA that’s a
                      real MBA at a price that’s achievable. This program is
                      totally disrupting higher education."
                    </Typography>
                  </div>
                </Grid>
                <Grid item xs={12} className="mobile-section-three-opinions">
                  <div>
                    <div
                      className="center"
                      style={{
                        width: "100%",
                      }}
                    >
                      <Avatar
                        src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/webp/11.webp"
                        alt="three-avatar"
                        className="mobile-section-three-avatar"
                        loading="lazy"
                      />
                    </div>
                    <Typography variant="h5">John</Typography>
                    <Typography variant="subtitle1">
                      Universita' Bocconi di Milano
                    </Typography>
                    <div className="separator-mobile"></div>
                    <Typography variant="subtitle2">
                      "I started at stage zero. With YouTorial I was able to
                      start learning online and eventually build up enough
                      knowledge and skills to transition into a well-paying
                      career."
                    </Typography>
                  </div>
                </Grid>
                <Grid item xs={12} className="mobile-section-three-opinions">
                  <div>
                    <div
                      className="center"
                      style={{
                        width: "100%",
                      }}
                    >
                      <Avatar
                        src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/webp/girl.webp"
                        alt="girl-image"
                        className="mobile-section-three-avatar"
                        loading="lazy"
                      />
                    </div>
                    <Typography variant="h5">Alice</Typography>
                    <Typography variant="subtitle1">
                      Universita' La Sapienza Roma
                    </Typography>
                    <div className="separator-mobile"></div>
                    <Typography variant="subtitle2">
                      "Recruiters saw my Professional Certificate on my LinkedIn
                      profile. During the interview, they told me they were
                      impressed with the skills I learned. I got the job!"
                    </Typography>
                  </div>
                </Grid>
              </Grid>
            </section>
          </div>
        </div>
      ) : (
        <div className="home-container">
          <section className="section-one">
            <Grid container style={{ height: "100vh" }}>
              <Grid item xs={12} className="center-and-align">
                <div>
                  <div style={{ textAlign: "center" }}>
                    <Typography variant="h1">YouTorial</Typography>
                    <Typography variant="h3">The future is yours!</Typography>
                  </div>
                  <div className="home-description-container">
                    <Typography variant="h5">
                      Revolutionary platform for the best e-learning experience
                      you can find anywhere!
                    </Typography>
                  </div>
                  <div className="start-btn-container">
                    <button
                      className="learn-more btn"
                      onClick={() => history.push("/feed")}
                    >
                      <span className="circle" aria-hidden="true">
                        <span className="icon arrow"></span>
                      </span>
                      <span className="button-text">Learn More</span>
                    </button>
                  </div>
                </div>
              </Grid>
            </Grid>
          </section>
          <Instructions />
          <section className="section-two">
            <Grid container className="section-two-container">
              <Grid item xs={6}>
                <Typography variant="h5">Sky's the limit!</Typography>
                <Typography variant="subtitle1">
                  Learn from tutors and teachers from all around the world about
                  the most various subjects, from coding to cooking up to music
                  production.
                  <br></br> Be a student or be a teacher. The choice is yours!
                </Typography>
                <div style={{ marginTop: "5rem" }}>
                  <img
                    src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/webp/online-class.webp"
                    alt="onlineclass"
                    loading="lazy"
                  />
                </div>
              </Grid>
              <Grid item xs={6}>
                <div>
                  <img
                    src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/webp/learning.webp"
                    alt="5"
                    loading="lazy"
                  />
                </div>
                <Typography variant="subtitle1" style={{ marginTop: "5rem" }}>
                  Build useful skills for school, work, university, or just for
                  the sake of learning something new. Easy-to-use interface and
                  limitless functionalities...
                </Typography>
              </Grid>
            </Grid>
          </section>

          <section className="section-three">
            <Grid container>
              <Grid item xs={12}>
                <div>
                  <Typography variant="h3">Get Ready for Take‑Off</Typography>
                  <img
                    src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/webp/clipart-rocket-9.webp"
                    alt="rocket"
                    loading="lazy"
                    style={{ marginTop: "-0.3rem" }}
                  />
                </div>
                <Typography variant="h5">
                  Give yourself an upgrade and join our growing community of
                  learners and teachers
                </Typography>
              </Grid>
              <Grid item xs={4} className="fourth-section-img-container">
                <Avatar
                  src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/webp/12.webp"
                  alt="12.jpg"
                  loading="lazy"
                />
                <Typography variant="h4">Mike</Typography>
                <Typography variant="subtitle1">
                  University of Bologna
                </Typography>
                <div className="separator"></div>
                <Typography variant="subtitle2">
                  "It’s a really big deal to offer an online MBA that’s a real
                  MBA at a price that’s achievable. This program is totally
                  disrupting higher education."
                </Typography>
              </Grid>
              <Grid item xs={4} className="fourth-section-img-container">
                <Avatar
                  src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/webp/11.webp"
                  alt="John"
                  loading="lazy"
                />
                <Typography variant="h4">John</Typography>
                <Typography variant="subtitle1">
                  Universita' Bocconi di Milano
                </Typography>
                <div className="separator"></div>
                <Typography variant="subtitle2">
                  "I started at stage zero. With YouTorial I was able to start
                  learning online and eventually build up enough knowledge and
                  skills to transition into a well-paying career."
                </Typography>
              </Grid>
              <Grid item xs={4} className="fourth-section-img-container">
                <Avatar
                  src="https://image-bucket-youtorial.s3.eu-central-1.amazonaws.com/webp/girl.webp"
                  alt="Alice"
                  loading="lazy"
                />
                <Typography variant="h4">Alice</Typography>
                <Typography variant="subtitle1">
                  Universita' La Sapienza Roma
                </Typography>
                <div className="separator"></div>
                <Typography variant="subtitle2">
                  "Recruiters saw my Professional Certificate on my LinkedIn
                  profile. During the interview, they told me they were
                  impressed with the skills I learned. I got the job!"
                </Typography>
              </Grid>
            </Grid>
          </section>
        </div>
      )}
    </>
  );
}
