import { Typography, Grid } from "@mui/material";
import React, { useState, useEffect } from "react";
import {
  FaFacebook,
  FaTwitterSquare,
  FaInstagram,
} from "react-icons/fa";
import { useMediaQuery } from "react-responsive";
import { useLocation } from "react-router-dom";

export default function Footer() {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });
  const [accountPage, setAccountPage] = useState(false);

  const location = useLocation();

  useEffect(() => {
    if (location.pathname.includes("/account")) setAccountPage(true);
    else setAccountPage(false);
  }, [location.pathname]);

  function RenderDesktop() {
    return (
      <div className="footer">
        <Grid container>
          <Grid item xs={12} className="footer-grid">
            <Typography variant="h6" style={{ marginBottom: "2rem" }}>
              Follow us on our socials
            </Typography>
            <div className="socials">
              <a
                href="https://www.facebook.com/profile.php?id=100084644521413"
                target="_blank"
                rel="noreferrer"
                className="hidelink"
              >
                <FaFacebook className="social-icon" />
              </a>
              <a
                href="https://twitter.com/youtorialorg"
                target="_blank"
                rel="noreferrer"
                className="hidelink"
              >
                <FaTwitterSquare className="social-icon" />
              </a>
              <a
                href="https://www.instagram.com/youtorialorg/"
                target="_blank"
                rel="noreferrer"
                className="hidelink"
              >
                <FaInstagram className="social-icon" />
              </a>
            </div>
          </Grid>
          <Grid item xs={12} className="center">
            <Typography variant="subtitle2">
              YouTorial © {new Date().getFullYear()}
            </Typography>
          </Grid>
        </Grid>
      </div>
    );
  }

  function RenderMobile() {
    return (
      <div className="footer-mobile">
        <Grid container>
          <Grid item xs={12}>
            <Typography variant="h6" className="footer-mobile-text">
              Follow us on our socials
            </Typography>
            <div className="footer-mobile-social-container">
              <a
                href="https://www.facebook.com/profile.php?id=100084644521413"
                target="_blank"
                rel="noreferrer"
                className="hidelink"
              >
                <FaFacebook className="footer-social-icons-mobile" />
              </a>
              <a
                href="https://twitter.com/youtorialorg"
                target="_blank"
                rel="noreferrer"
                className="hidelink"
              >
                <FaTwitterSquare className="footer-social-icons-mobile" />
              </a>
              <a
                href="https://www.instagram.com/youtorialorg/"
                target="_blank"
                rel="noreferrer"
                className="hidelink"
              >
                <FaInstagram className="footer-social-icons-mobile" />
              </a>
            </div>
            <div className="footer-mobile-copyright">
              <Typography variatn="subtitle2">
                YouTorial © {new Date().getFullYear()}
              </Typography>
            </div>
          </Grid>
        </Grid>
      </div>
    );
  }

  return (
    <footer style={{ display: accountPage ? "none" : "block" }}>
      {isTabletOrMobile ? <RenderMobile /> : <RenderDesktop />}
    </footer>
  );
}
