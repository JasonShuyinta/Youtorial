import React, { useState, useEffect } from "react";
import { Snackbar, Slide, Alert, Stack } from "@mui/material";

function TransitionRight(props) {
  return <Slide {...props} direction="right" />;
}

export default function Snack({ message, successful }) {
  const [open, setOpen] = useState(false);

  useEffect(() => {
    setOpen(successful);
  }, [successful]);

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <>
      <Snackbar
        open={open}
        onClose={handleClose}
        TransitionComponent={TransitionRight}
        autoHideDuration={2000}
      >
        <Stack>
          <Alert severity="success">{message}</Alert>
        </Stack>
      </Snackbar>
    </>
  );
}
