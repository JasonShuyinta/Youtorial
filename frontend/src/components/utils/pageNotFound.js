import React from "react";
import { Box, Container, Button } from "@mui/material";
import { Link } from "react-router-dom"

export default function PageNotFound() {
  return (
    <div className="full-height mt-4">
      <Container>
        <Box>
          <h1 className="title-404">404</h1>

          <h2>Oops! This Page Could Not Be Found</h2>
          <p>
            SORRY BUT THE PAGE YOU ARE LOOKING FOR DOES NOT EXIST, HAVE BEEN
            REMOVED. NAME CHANGED OR IS TEMPORARILY UNAVAILABLE
          </p>
          <Button variant="contained" className="btn-style">
            <Link to="/" className="hidelink">
            Return to homepage
            </Link>
          </Button>

        </Box>
      </Container>
    </div>
  );
}
