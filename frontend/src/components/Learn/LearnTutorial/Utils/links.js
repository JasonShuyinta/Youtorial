import React from "react";
import { Grid, Chip, Divider } from "@mui/material";
import { AiOutlineLink } from "react-icons/ai";
import { useMediaQuery } from "react-responsive";

export default function Links({ links }) {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 900px)" });

  return (
    <Grid container>
      <Grid item xs={12}>
        <Divider style={{ margin: "1rem 0" }} />
        <div style={{ display: links && links.length !== 0 ? "flex" : "none" }}>
          <AiOutlineLink
            width="15px"
            height="15px"
            style={{ marginTop: "0.4rem", marginRight: "0.4rem" }}
          />
          {isTabletOrMobile ? (
            <Grid container spacing={2}>
              {links &&
                links.length > 0 &&
                links.map((link, index) => {
                  return (
                    link.link !== "" && (
                      <Grid item xs={6} key={index}>
                        <Chip
                          label={link.name}
                          onClick={() => window.open(link.link)}
                          clickable
                          size="small"
                          variant="outlined"
                          className="chipStyle"
                        />
                      </Grid>
                    )
                  );
                })}
            </Grid>
          ) : (
            <>
              {links &&
                links.length > 0 &&
                links.map((link, index) => {
                  return (
                    link.link !== "" && (
                      <Chip
                        label={link.name}
                        key={index}
                        onClick={() => window.open(link.link)}
                        clickable
                        size="small"
                        variant="outlined"
                        className="chipStyle"
                      />
                    )
                  );
                })}
            </>
          )}
        </div>
      </Grid>
    </Grid>
  );
}
